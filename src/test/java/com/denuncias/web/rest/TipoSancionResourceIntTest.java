package com.denuncias.web.rest;

import com.denuncias.Application;
import com.denuncias.domain.TipoSancion;
import com.denuncias.repository.TipoSancionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TipoSancionResource REST controller.
 *
 * @see TipoSancionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TipoSancionResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final Double DEFAULT_COSTO = 0D;
    private static final Double UPDATED_COSTO = 1D;

    @Inject
    private TipoSancionRepository tipoSancionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTipoSancionMockMvc;

    private TipoSancion tipoSancion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TipoSancionResource tipoSancionResource = new TipoSancionResource();
        ReflectionTestUtils.setField(tipoSancionResource, "tipoSancionRepository", tipoSancionRepository);
        this.restTipoSancionMockMvc = MockMvcBuilders.standaloneSetup(tipoSancionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tipoSancionRepository.deleteAll();
        tipoSancion = new TipoSancion();
        tipoSancion.setNombre(DEFAULT_NOMBRE);
        tipoSancion.setCosto(DEFAULT_COSTO);
    }

    @Test
    public void createTipoSancion() throws Exception {
        int databaseSizeBeforeCreate = tipoSancionRepository.findAll().size();

        // Create the TipoSancion

        restTipoSancionMockMvc.perform(post("/api/tipoSancions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoSancion)))
                .andExpect(status().isCreated());

        // Validate the TipoSancion in the database
        List<TipoSancion> tipoSancions = tipoSancionRepository.findAll();
        assertThat(tipoSancions).hasSize(databaseSizeBeforeCreate + 1);
        TipoSancion testTipoSancion = tipoSancions.get(tipoSancions.size() - 1);
        assertThat(testTipoSancion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoSancion.getCosto()).isEqualTo(DEFAULT_COSTO);
    }

    @Test
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoSancionRepository.findAll().size();
        // set the field null
        tipoSancion.setNombre(null);

        // Create the TipoSancion, which fails.

        restTipoSancionMockMvc.perform(post("/api/tipoSancions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoSancion)))
                .andExpect(status().isBadRequest());

        List<TipoSancion> tipoSancions = tipoSancionRepository.findAll();
        assertThat(tipoSancions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCostoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoSancionRepository.findAll().size();
        // set the field null
        tipoSancion.setCosto(null);

        // Create the TipoSancion, which fails.

        restTipoSancionMockMvc.perform(post("/api/tipoSancions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoSancion)))
                .andExpect(status().isBadRequest());

        List<TipoSancion> tipoSancions = tipoSancionRepository.findAll();
        assertThat(tipoSancions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTipoSancions() throws Exception {
        // Initialize the database
        tipoSancionRepository.save(tipoSancion);

        // Get all the tipoSancions
        restTipoSancionMockMvc.perform(get("/api/tipoSancions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tipoSancion.getId())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].costo").value(hasItem(DEFAULT_COSTO.doubleValue())));
    }

    @Test
    public void getTipoSancion() throws Exception {
        // Initialize the database
        tipoSancionRepository.save(tipoSancion);

        // Get the tipoSancion
        restTipoSancionMockMvc.perform(get("/api/tipoSancions/{id}", tipoSancion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tipoSancion.getId()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.costo").value(DEFAULT_COSTO.doubleValue()));
    }

    @Test
    public void getNonExistingTipoSancion() throws Exception {
        // Get the tipoSancion
        restTipoSancionMockMvc.perform(get("/api/tipoSancions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTipoSancion() throws Exception {
        // Initialize the database
        tipoSancionRepository.save(tipoSancion);

		int databaseSizeBeforeUpdate = tipoSancionRepository.findAll().size();

        // Update the tipoSancion
        tipoSancion.setNombre(UPDATED_NOMBRE);
        tipoSancion.setCosto(UPDATED_COSTO);

        restTipoSancionMockMvc.perform(put("/api/tipoSancions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoSancion)))
                .andExpect(status().isOk());

        // Validate the TipoSancion in the database
        List<TipoSancion> tipoSancions = tipoSancionRepository.findAll();
        assertThat(tipoSancions).hasSize(databaseSizeBeforeUpdate);
        TipoSancion testTipoSancion = tipoSancions.get(tipoSancions.size() - 1);
        assertThat(testTipoSancion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoSancion.getCosto()).isEqualTo(UPDATED_COSTO);
    }

    @Test
    public void deleteTipoSancion() throws Exception {
        // Initialize the database
        tipoSancionRepository.save(tipoSancion);

		int databaseSizeBeforeDelete = tipoSancionRepository.findAll().size();

        // Get the tipoSancion
        restTipoSancionMockMvc.perform(delete("/api/tipoSancions/{id}", tipoSancion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TipoSancion> tipoSancions = tipoSancionRepository.findAll();
        assertThat(tipoSancions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
