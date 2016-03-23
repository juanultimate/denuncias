package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.Denuncia;
import com.denuncias.repository.CantonRepository;
import com.denuncias.repository.DenunciaRepository;
import com.denuncias.web.rest.util.PaginationUtil;
import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import javax.inject.Inject;
import java.net.URISyntaxException;
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

    class Data{
        String label;
        Integer Value;
    }


    /**
     * GET  /denuncias -> get all the denuncias.
     */
    @RequestMapping(value = "/reportes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Data>> getData(@RequestParam(value="tipo", required=true) String tipo)throws URISyntaxException {
        log.debug("REST request to get a Data Report");
        mongoTemplate.find(query(where("canton.nombre").lt(50)
            .and("accounts.balance").gt(1000.00d)), Person.class)

        List<Data> datas = denunciaRepository.findByEstadoLike(estado, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/denuncias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }



}
