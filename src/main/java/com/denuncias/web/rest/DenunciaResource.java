package com.denuncias.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.denuncias.domain.Canton;
import com.denuncias.domain.Denuncia;
import com.denuncias.domain.enumeration.Estado;
import com.denuncias.repository.CantonRepository;
import com.denuncias.repository.DenunciaRepository;
import com.denuncias.service.denuncias.CodeGenerator;
import com.denuncias.web.rest.util.HeaderUtil;
import com.denuncias.web.rest.util.LocationUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
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

    @Inject
    CantonRepository cantonRepository;

    @Inject
    CodeGenerator codeGenerator;



    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public @ResponseBody ResponseEntity<Denuncia> handleFileUpload(@RequestParam("lat") String lat,
                             @RequestParam("lon") String lon,
                             @RequestParam("key") MultipartFile file)  {
        try {
            Denuncia denuncia = new Denuncia();
            denuncia.setCodigo(codeGenerator.generarCodigo());
            denuncia.setLatitud(lat);
            denuncia.setLongitud(lon);
            denuncia.setFoto(file.getBytes());
            denuncia.setFotoContentType("image/jpeg");
            return this.createDenuncia(denuncia);
        }catch (Exception e){
            log.error("Error creando denuncia", e);
            return new ResponseEntity<Denuncia>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST  /denuncias -> Create a new denuncia.
     */
    @RequestMapping(value = "/denuncias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Denuncia> createDenuncia(@RequestBody Denuncia denuncia) throws URISyntaxException, IOException {
        log.debug("REST request to save Denuncia : {}", denuncia);
        if (denuncia.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("denuncia", "idexists", "A new denuncia cannot already have an ID")).body(null);
        }

        Canton canton = LocationUtil.getCanton(denuncia.getLatitud(),denuncia.getLongitud());
        List<Canton> cantones = cantonRepository.findByCodigo(canton.getCodigo());
        if(!cantones.isEmpty()){
            canton = cantones.get(0);
        }
        else{
            canton = cantonRepository.save(canton);
        }
        denuncia.setDireccion(LocationUtil.getDireccion(denuncia.getLatitud(),denuncia.getLongitud()));
        denuncia.setCanton(canton);
        denuncia.setFecha(ZonedDateTime.now());
        denuncia.setEstado(Estado.Creada);
        denuncia.setPagado(false);
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
    public ResponseEntity<Denuncia> updateDenuncia(@RequestBody Denuncia denuncia) throws URISyntaxException, IOException {
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
    public ResponseEntity<List<Denuncia>> getAllDenuncias(Pageable pageable, @RequestParam(value="estado", required=false) String estado)
        throws URISyntaxException {
        log.debug("REST request to get a page of Denuncias");
        Page<Denuncia> page = denunciaRepository.findByEstadoLike(estado, pageable);
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
