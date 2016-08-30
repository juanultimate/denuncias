package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.Canton;
import com.denuncias.domain.Denuncia;
import com.denuncias.domain.ReportData;
import com.denuncias.domain.enumeration.DiaSemana;
import com.denuncias.repository.CantonRepository;
import com.denuncias.repository.DenunciaRepository;
import com.denuncias.service.UserService;
import com.denuncias.web.rest.util.PaginationUtil;
import com.mongodb.*;
import org.apache.commons.lang3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.MessageUtils;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by JuanGabriel on 23/3/2016.
 */
@RestController
@RequestMapping("/api")
@Component

public class ReporteResource {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ReporteResource(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }



    private final Logger log = LoggerFactory.getLogger(DenunciaResource.class);

    @Inject
    private DenunciaRepository denunciaRepository;

    @Inject
    CantonRepository cantonRepository;

    @Inject
    private UserService userService;



    /**
     * GET  /denuncias -> get all the denuncias.
     */
    @RequestMapping(value = "/reportes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReportData>> getData(@RequestParam(value="tipo", required=false) String tipo)throws URISyntaxException {
        log.debug("REST request to get a Data Report");
        log.debug("Current user: {}", userService.getUserWithAuthorities().getFirstName());
        List<ReportData> data = new ArrayList<ReportData>();
        switch (tipo){
            case "dia":{
                DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("$dayOfWeek", "$fecha"));
                groupFields.put("value", new BasicDBObject( "$sum", 1));
                DBObject group = new BasicDBObject("$group", groupFields);
                List pipeline = Arrays.asList(group);
                DBCollection collection = mongoTemplate.getCollection("denuncia");
                AggregationOutput output = collection.aggregate(pipeline);
                for (DBObject item :output.results()) {
                    data.add(new ReportData(StringUtils.capitalize(DiaSemana.fromInteger(Integer.valueOf(item.get("_id").toString())).toString()),Long.valueOf(item.get("value").toString())));
                }
                break;
            }
            case "pago":{
                Long countPagado = denunciaRepository.countByPagado(true);
                data.add(new ReportData("Pagado", countPagado));
                Long countNoPagado = denunciaRepository.countByPagado(false);
                data.add(new ReportData("No pagado", countNoPagado));
                break;
            }
            case "mes":{
                DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("$month", "$fecha"));
                groupFields.put("value", new BasicDBObject( "$sum", 1));
                DBObject group = new BasicDBObject("$group", groupFields);
                List pipeline = Arrays.asList(group);
                DBCollection collection = mongoTemplate.getCollection("denuncia");
                AggregationOutput output = collection.aggregate(pipeline);
                for (DBObject item :output.results()) {
                    String mes = new DateFormatSymbols(Locale.forLanguageTag("es")).getMonths()[(Integer.valueOf(item.get("_id").toString())-1)];
                    data.add(new ReportData(StringUtils.capitalize(mes),Long.valueOf(item.get("value").toString())));
                }
                break;
            }

            case "anio":{
                DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("$year", "$fecha"));
                groupFields.put("value", new BasicDBObject( "$sum", 1));
                DBObject group = new BasicDBObject("$group", groupFields);
                List pipeline = Arrays.asList(group);
                DBCollection collection = mongoTemplate.getCollection("denuncia");
                AggregationOutput output = collection.aggregate(pipeline);
                for (DBObject item :output.results()) {
                    data.add(new ReportData(item.get("_id").toString(),Long.valueOf(item.get("value").toString())));
                }
                break;
            }
            default:{
                break;
            }
        }
        return new ResponseEntity<>(data,HttpStatus.OK);
    }
}
