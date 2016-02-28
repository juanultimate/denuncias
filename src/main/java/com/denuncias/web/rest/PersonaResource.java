package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.Persona;
import com.denuncias.repository.PersonaRepository;
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
 * REST controller for managing Persona.
 */
@RestController
@RequestMapping("/api")
public class PersonaResource {

    private final Logger log = LoggerFactory.getLogger(PersonaResource.class);
        
    @Inject
    private PersonaRepository personaRepository;
    
    /**
     * POST  /personas -> Create a new persona.
     */
    @RequestMapping(value = "/personas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) throws URISyntaxException {
        log.debug("REST request to save Persona : {}", persona);
        if (persona.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("persona", "idexists", "A new persona cannot already have an ID")).body(null);
        }
        Persona result = personaRepository.save(persona);
        return ResponseEntity.created(new URI("/api/personas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("persona", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personas -> Updates an existing persona.
     */
    @RequestMapping(value = "/personas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Persona> updatePersona(@RequestBody Persona persona) throws URISyntaxException {
        log.debug("REST request to update Persona : {}", persona);
        if (persona.getId() == null) {
            return createPersona(persona);
        }
        Persona result = personaRepository.save(persona);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("persona", persona.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personas -> get all the personas.
     */
    @RequestMapping(value = "/personas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Persona> getAllPersonas() {
        log.debug("REST request to get all Personas");
        return personaRepository.findAll();
            }

    /**
     * GET  /personas/:id -> get the "id" persona.
     */
    @RequestMapping(value = "/personas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Persona> getPersona(@PathVariable String id) {
        log.debug("REST request to get Persona : {}", id);
        Persona persona = personaRepository.findOne(id);
        return Optional.ofNullable(persona)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personas/:id -> delete the "id" persona.
     */
    @RequestMapping(value = "/personas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersona(@PathVariable String id) {
        log.debug("REST request to delete Persona : {}", id);
        personaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("persona", id.toString())).build();
    }
}
