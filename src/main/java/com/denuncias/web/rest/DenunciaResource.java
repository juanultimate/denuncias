package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.Denuncia;
import com.denuncias.repository.DenunciaRepository;
import com.denuncias.web.rest.util.HeaderUtil;
import com.denuncias.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Denuncia.
 */
@RestController
@RequestMapping("/api")
public class DenunciaResource {

    private final Logger log = LoggerFactory.getLogger(DenunciaResource.class);
        
    @Inject
    private DenunciaRepository denunciaRepository;
    
    /**
     * POST  /denuncias -> Create a new denuncia.
     */
    @RequestMapping(value = "/denuncias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Denuncia> createDenuncia(@RequestBody Denuncia denuncia) throws URISyntaxException {
        log.debug("REST request to save Denuncia : {}", denuncia);
        if (denuncia.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("denuncia", "idexists", "A new denuncia cannot already have an ID")).body(null);
        }
        Denuncia result = denunciaRepository.save(denuncia);
        return ResponseEntity.created(new URI("/api/denuncias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("denuncia", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /denuncias -> Updates an existing denuncia.
     */
    @RequestMapping(value = "/denuncias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Denuncia> updateDenuncia(@RequestBody Denuncia denuncia) throws URISyntaxException {
        log.debug("REST request to update Denuncia : {}", denuncia);
        if (denuncia.getId() == null) {
            return createDenuncia(denuncia);
        }
        Denuncia result = denunciaRepository.save(denuncia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("denuncia", denuncia.getId().toString()))
            .body(result);
    }

    /**
     * GET  /denuncias -> get all the denuncias.
     */
    @RequestMapping(value = "/denuncias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Denuncia>> getAllDenuncias(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Denuncias");
        Page<Denuncia> page = denunciaRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/denuncias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /denuncias/:id -> get the "id" denuncia.
     */
    @RequestMapping(value = "/denuncias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Denuncia> getDenuncia(@PathVariable String id) {
        log.debug("REST request to get Denuncia : {}", id);
        Denuncia denuncia = denunciaRepository.findOne(id);
        return Optional.ofNullable(denuncia)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /denuncias/:id -> delete the "id" denuncia.
     */
    @RequestMapping(value = "/denuncias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDenuncia(@PathVariable String id) {
        log.debug("REST request to delete Denuncia : {}", id);
        denunciaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("denuncia", id.toString())).build();
    }
}
