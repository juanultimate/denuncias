package com.denuncias.web.rest;

import com.denuncias.Application;
import com.denuncias.domain.Canton;
import com.denuncias.repository.CantonRepository;

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
 * Test class for the CantonResource REST controller.
 *
 * @see CantonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CantonResourceIntTest {

    private static final String DEFAULT_CODIGO = "AAAAA";
    private static final String UPDATED_CODIGO = "BBBBB";
    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    @Inject
    private CantonRepository cantonRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCantonMockMvc;

    private Canton canton;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CantonResource cantonResource = new CantonResource();
        ReflectionTestUtils.setField(cantonResource, "cantonRepository", cantonRepository);
        this.restCantonMockMvc = MockMvcBuilders.standaloneSetup(cantonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cantonRepository.deleteAll();
        canton = new Canton();
        canton.setCodigo(DEFAULT_CODIGO);
        canton.setNombre(DEFAULT_NOMBRE);
    }

    @Test
    public void createCanton() throws Exception {
        int databaseSizeBeforeCreate = cantonRepository.findAll().size();

        // Create the Canton

        restCantonMockMvc.perform(post("/api/cantons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(canton)))
                .andExpect(status().isCreated());

        // Validate the Canton in the database
        List<Canton> cantons = cantonRepository.findAll();
        assertThat(cantons).hasSize(databaseSizeBeforeCreate + 1);
        Canton testCanton = cantons.get(cantons.size() - 1);
        assertThat(testCanton.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCanton.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    public void getAllCantons() throws Exception {
        // Initialize the database
        cantonRepository.save(canton);

        // Get all the cantons
        restCantonMockMvc.perform(get("/api/cantons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(canton.getId())))
                .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    public void getCanton() throws Exception {
        // Initialize the database
        cantonRepository.save(canton);

        // Get the canton
        restCantonMockMvc.perform(get("/api/cantons/{id}", canton.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(canton.getId()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    public void getNonExistingCanton() throws Exception {
        // Get the canton
        restCantonMockMvc.perform(get("/api/cantons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCanton() throws Exception {
        // Initialize the database
        cantonRepository.save(canton);

		int databaseSizeBeforeUpdate = cantonRepository.findAll().size();

        // Update the canton
        canton.setCodigo(UPDATED_CODIGO);
        canton.setNombre(UPDATED_NOMBRE);

        restCantonMockMvc.perform(put("/api/cantons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(canton)))
                .andExpect(status().isOk());

        // Validate the Canton in the database
        List<Canton> cantons = cantonRepository.findAll();
        assertThat(cantons).hasSize(databaseSizeBeforeUpdate);
        Canton testCanton = cantons.get(cantons.size() - 1);
        assertThat(testCanton.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCanton.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    public void deleteCanton() throws Exception {
        // Initialize the database
        cantonRepository.save(canton);

		int databaseSizeBeforeDelete = cantonRepository.findAll().size();

        // Get the canton
        restCantonMockMvc.perform(delete("/api/cantons/{id}", canton.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Canton> cantons = cantonRepository.findAll();
        assertThat(cantons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
