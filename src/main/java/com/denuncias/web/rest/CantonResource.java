package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.Canton;
import com.denuncias.repository.CantonRepository;
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
 * REST controller for managing Canton.
 */
@RestController
@RequestMapping("/api")
public class CantonResource {

    private final Logger log = LoggerFactory.getLogger(CantonResource.class);

    @Inject
    private CantonRepository cantonRepository;

    /**
     * POST  /cantons -> Create a new canton.
     */
    @RequestMapping(value = "/cantons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Canton> createCanton(@RequestBody Canton canton) throws URISyntaxException {
        log.debug("REST request to save Canton : {}", canton);
        if (canton.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("canton", "idexists", "A new canton cannot already have an ID")).body(null);
        }
        Canton result = cantonRepository.save(canton);
        return ResponseEntity.created(new URI("/api/cantons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cantón", result.getCodigo().toString()))
            .body(result);
    }

    /**
     * PUT  /cantons -> Updates an existing canton.
     */
    @RequestMapping(value = "/cantons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Canton> updateCanton(@RequestBody Canton canton) throws URISyntaxException {
        log.debug("REST request to update Canton : {}", canton);
        if (canton.getId() == null) {
            return createCanton(canton);
        }
        Canton result = cantonRepository.save(canton);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("canton", canton.getCodigo().toString()))
            .body(result);
    }

    /**
     * GET  /cantons -> get all the cantons.
     */
    @RequestMapping(value = "/cantons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Canton>> getAllCantons(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cantons");
        Page<Canton> page = cantonRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cantons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cantons/:id -> get the "id" canton.
     */
    @RequestMapping(value = "/cantons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Canton> getCanton(@PathVariable String id) {
        log.debug("REST request to get Canton : {}", id);
        Canton canton = cantonRepository.findOne(id);
        return Optional.ofNullable(canton)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cantons/:id -> delete the "id" canton.
     */
    @RequestMapping(value = "/cantons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCanton(@PathVariable String id) {
        log.debug("REST request to delete Canton : {}", id);
        cantonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("canton", id.toString())).build();
    }
}
