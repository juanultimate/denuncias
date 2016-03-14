package com.denuncias.web.rest.util;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by JuanGabriel on 13/3/2016.
 */
public class LocationUtil {

    private static void getCanton(String latitud, String longitud) throws IOException {
        String url = "http://overpass-api.de/api/interpreter";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String data = String.format("data=[timeout:5][out:json];is_in(%1$s,%2$s)->.a;way(pivot.a);out tags geom(%1$s,%2$s,%1$s,%2$s);relation(pivot.a);out tags bb;", latitud,longitud );
        post.setEntity(new StringEntity(data));
        HttpResponse response = client.execute(post);
        if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
            StringBuffer result = getResponseBody(response);
            JSONObject jsonObj2 = new JSONObject(result.toString());
            JSONArray lista = jsonObj2.getJSONArray("elements");
            for (int i = 0; i < lista.length(); i++) {
                if (lista.getJSONObject(i).getJSONObject("tags").has("county_code")) {
                    String codigoCanton = lista.getJSONObject(i).getJSONObject("tags").getString("county_code");
                    String nombreCanton = lista.getJSONObject(i).getJSONObject("tags").getString("name");
                    System.out.println(codigoCanton);
                    System.out.println(nombreCanton);
                    break;
                }
            }
        }
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


    private static void getDireccion(String latitud, String longitud) throws IOException {
        String url = String.format("http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$s,%2$s",latitud,longitud);
        System.out.println(url);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
            StringBuffer result = getResponseBody(response);
            JSONObject jsonObj2 = new JSONObject(result.toString());
            for (int i = 0; i < jsonObj2.getJSONArray("results").length(); i++) {
                if (jsonObj2.getJSONArray("results").getJSONObject(i).has("types") && jsonObj2.getJSONArray("results").getJSONObject(i).getJSONArray("types").get(0).equals("route")) {
                    System.out.println(jsonObj2.getJSONArray("results").getJSONObject(i).get("formatted_address"));
                    break;
                }

            }
        }


    }
