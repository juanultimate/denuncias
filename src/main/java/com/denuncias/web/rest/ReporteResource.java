package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.Canton;
import com.denuncias.domain.Denuncia;
import com.denuncias.domain.ReportData;
import com.denuncias.repository.CantonRepository;
import com.denuncias.repository.DenunciaRepository;
import com.denuncias.web.rest.util.PaginationUtil;
import com.mongodb.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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



    /**
     * GET  /denuncias -> get all the denuncias.
     */
    @RequestMapping(value = "/reportes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReportData>> getData(@RequestParam(value="tipo", required=false) String tipo)throws URISyntaxException {
        log.debug("REST request to get a Data Report");
        List<ReportData> data = new ArrayList<ReportData>();
        for (Canton canton:cantonRepository.findAll()) {
            Long count = denunciaRepository.countByCanton(canton);
            data.add(new ReportData(canton.getNombre(), count));
        }
        return new ResponseEntity<>(data,HttpStatus.OK);
    }



    /**
     * GET  /denuncias -> get all the denuncias.
     */
    @RequestMapping(value = "/reportes2",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReportData>> getData2(@RequestParam(value="tipo", required=false) String tipo)throws URISyntaxException {
        log.debug("REST request to get a Data Report");
        List<ReportData> data = new ArrayList<ReportData>();
        Long countPagado = denunciaRepository.countByPagado(true);
        data.add(new ReportData("Pagado", countPagado));
        Long countNoPagado = denunciaRepository.countByPagado(false);
        data.add(new ReportData("No pagado", countNoPagado));
        return new ResponseEntity<>(data,HttpStatus.OK);
    }



    /**
     * GET  /denuncias -> get all the denuncias.
     */
    @RequestMapping(value = "/reportes3",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReportData>> getData3(@RequestParam(value="tipo", required=false) String tipo)throws URISyntaxException {
        log.debug("REST request to get a Data Report");
        DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("$dayOfWeek", "$fecha"));
        groupFields.put("value", new BasicDBObject( "$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);
        List pipeline = Arrays.asList(group);
        DBCollection collection = mongoTemplate.getCollection("denuncia");
        AggregationOutput output = collection.aggregate(pipeline);
        for (DBObject item :output.results()) {

        }

        List<ReportData> data = new ArrayList<ReportData>();

        return new ResponseEntity<>(data,HttpStatus.OK);

    }


}
