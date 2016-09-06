package com.denuncias.web.rest.util;

import com.denuncias.domain.Canton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by JuanGabriel on 13/3/2016.
 */

public class LocationUtil {
    private final static Logger log = LoggerFactory.getLogger(LocationUtil.class);

    public static final String HTTP_OVERPASS_API_DE_API_INTERPRETER = "http://overpass-api.de/api/interpreter";
    public static final String HTTP_MAPS_GOOGLEAPIS_COM_MAPS_API_GEOCODE_JSON = "http://maps.googleapis.com/maps/api/geocode/json";

    public static Canton getCanton(String latitud, String longitud) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(HTTP_OVERPASS_API_DE_API_INTERPRETER);
        String data = String.format("data=[timeout:5][out:json];is_in(%1$s,%2$s)->.a;way(pivot.a);out tags geom(%1$s,%2$s,%1$s,%2$s);relation(pivot.a);out tags bb;", latitud, longitud);
        post.setEntity(new StringEntity(data));
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            StringBuffer result = getResponseBody(response);
            JSONObject jsonObj2 = new JSONObject(result.toString());
            String provincia = getProvincia(jsonObj2);
            JSONArray lista = jsonObj2.getJSONArray("elements");
            for (int i = 0; i < lista.length(); i++) {
                if (lista.getJSONObject(i).getJSONObject("tags").has("county_code")) {
                    String codigoCanton = lista.getJSONObject(i).getJSONObject("tags").getString("county_code");
                    String nombreCanton = lista.getJSONObject(i).getJSONObject("tags").getString("name");
                    Canton canton = new Canton(codigoCanton,nombreCanton,provincia);
                    log.info(canton.toString());
                    return canton;
                }
            }
        }
        return null;
    }

    private static String getProvincia(JSONObject jsonObj2) {
        JSONArray lista = jsonObj2.getJSONArray("elements");
        String provinciaCanton="";
        for (int i = 0; i < lista.length(); i++) {
            if (lista.getJSONObject(i).getJSONObject("tags").has("admin_level")&& lista.getJSONObject(i).getJSONObject("tags").get("admin_level").equals("6")) {
                provinciaCanton = lista.getJSONObject(i).getJSONObject("tags").getString("name");
                break;
            }
        }
        return provinciaCanton;
    }

    private static StringBuffer getResponseBody(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }


    public static String getDireccion(String latitud, String longitud) throws IOException {
        String url = String.format(HTTP_MAPS_GOOGLEAPIS_COM_MAPS_API_GEOCODE_JSON + "?latlng=%1$s,%2$s", latitud, longitud);
        System.out.println(url);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            StringBuffer result = getResponseBody(response);
            JSONObject jsonObj2 = new JSONObject(result.toString());
            for (int i = 0; i < jsonObj2.getJSONArray("results").length(); i++) {
                if (jsonObj2.getJSONArray("results").getJSONObject(i).has("types") && jsonObj2.getJSONArray("results").getJSONObject(i).getJSONArray("types").get(0).equals("route")) {
                  String direccion = jsonObj2.getJSONArray("results").getJSONObject(i).get("formatted_address").toString();
                  log.info("Adress:"+direccion);
                   return direccion;
                }
            }
        }
        return null;
    }
}
