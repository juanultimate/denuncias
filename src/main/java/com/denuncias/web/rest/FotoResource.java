package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.Foto;
import com.denuncias.repository.FotoRepository;
import com.denuncias.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Foto.
 */
@RestController
@RequestMapping("/api")
public class FotoResource {

    private final Logger log = LoggerFactory.getLogger(FotoResource.class);
        
    @Inject
    private FotoRepository fotoRepository;
    
    /**
     * POST  /fotos -> Create a new foto.
     */
    @RequestMapping(value = "/fotos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Foto> createFoto(@RequestBody Foto foto) throws URISyntaxException {
        log.debug("REST request to save Foto : {}", foto);
        if (foto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("foto", "idexists", "A new foto cannot already have an ID")).body(null);
        }
        Foto result = fotoRepository.save(foto);
        return ResponseEntity.created(new URI("/api/fotos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("foto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fotos -> Updates an existing foto.
     */
    @RequestMapping(value = "/fotos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Foto> updateFoto(@RequestBody Foto foto) throws URISyntaxException {
        log.debug("REST request to update Foto : {}", foto);
        if (foto.getId() == null) {
            return createFoto(foto);
        }
        Foto result = fotoRepository.save(foto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("foto", foto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fotos -> get all the fotos.
     */
    @RequestMapping(value = "/fotos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Foto> getAllFotos() {
        log.debug("REST request to get all Fotos");
        return fotoRepository.findAll();
            }

    /**
     * GET  /fotos/:id -> get the "id" foto.
     */
    @RequestMapping(value = "/fotos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Foto> getFoto(@PathVariable String id) {
        log.debug("REST request to get Foto : {}", id);
        Foto foto = fotoRepository.findOne(id);
        return Optional.ofNullable(foto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fotos/:id -> delete the "id" foto.
     */
    @RequestMapping(value = "/fotos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFoto(@PathVariable String id) {
        log.debug("REST request to delete Foto : {}", id);
        fotoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("foto", id.toString())).build();
    }
}
