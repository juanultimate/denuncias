package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.TipoSancion;
import com.denuncias.repository.TipoSancionRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TipoSancion.
 */
@RestController
@RequestMapping("/api")
public class TipoSancionResource {

    private final Logger log = LoggerFactory.getLogger(TipoSancionResource.class);
        
    @Inject
    private TipoSancionRepository tipoSancionRepository;
    
    /**
     * POST  /tipoSancions -> Create a new tipoSancion.
     */
    @RequestMapping(value = "/tipoSancions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoSancion> createTipoSancion(@Valid @RequestBody TipoSancion tipoSancion) throws URISyntaxException {
        log.debug("REST request to save TipoSancion : {}", tipoSancion);
        if (tipoSancion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tipoSancion", "idexists", "A new tipoSancion cannot already have an ID")).body(null);
        }
        TipoSancion result = tipoSancionRepository.save(tipoSancion);
        return ResponseEntity.created(new URI("/api/tipoSancions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tipoSancion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipoSancions -> Updates an existing tipoSancion.
     */
    @RequestMapping(value = "/tipoSancions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoSancion> updateTipoSancion(@Valid @RequestBody TipoSancion tipoSancion) throws URISyntaxException {
        log.debug("REST request to update TipoSancion : {}", tipoSancion);
        if (tipoSancion.getId() == null) {
            return createTipoSancion(tipoSancion);
        }
        TipoSancion result = tipoSancionRepository.save(tipoSancion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tipoSancion", tipoSancion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipoSancions -> get all the tipoSancions.
     */
    @RequestMapping(value = "/tipoSancions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TipoSancion>> getAllTipoSancions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TipoSancions");
        Page<TipoSancion> page = tipoSancionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tipoSancions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tipoSancions/:id -> get the "id" tipoSancion.
     */
    @RequestMapping(value = "/tipoSancions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TipoSancion> getTipoSancion(@PathVariable String id) {
        log.debug("REST request to get TipoSancion : {}", id);
        TipoSancion tipoSancion = tipoSancionRepository.findOne(id);
        return Optional.ofNullable(tipoSancion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tipoSancions/:id -> delete the "id" tipoSancion.
     */
    @RequestMapping(value = "/tipoSancions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTipoSancion(@PathVariable String id) {
        log.debug("REST request to delete TipoSancion : {}", id);
        tipoSancionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tipoSancion", id.toString())).build();
    }
}
