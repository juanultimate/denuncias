package com.denuncias.web.rest;

import com.denuncias.Application;
import com.denuncias.domain.Foto;
import com.denuncias.repository.FotoRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FotoResource REST controller.
 *
 * @see FotoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FotoResourceIntTest {

    private static final String DEFAULT_CODIGO = "AAAAA";
    private static final String UPDATED_CODIGO = "BBBBB";

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    @Inject
    private FotoRepository fotoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFotoMockMvc;

    private Foto foto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FotoResource fotoResource = new FotoResource();
        ReflectionTestUtils.setField(fotoResource, "fotoRepository", fotoRepository);
        this.restFotoMockMvc = MockMvcBuilders.standaloneSetup(fotoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fotoRepository.deleteAll();
        foto = new Foto();
        foto.setCodigo(DEFAULT_CODIGO);
        foto.setData(DEFAULT_DATA);
        foto.setDataContentType(DEFAULT_DATA_CONTENT_TYPE);
    }

    @Test
    public void createFoto() throws Exception {
        int databaseSizeBeforeCreate = fotoRepository.findAll().size();

        // Create the Foto

        restFotoMockMvc.perform(post("/api/fotos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(foto)))
                .andExpect(status().isCreated());

        // Validate the Foto in the database
        List<Foto> fotos = fotoRepository.findAll();
        assertThat(fotos).hasSize(databaseSizeBeforeCreate + 1);
        Foto testFoto = fotos.get(fotos.size() - 1);
        assertThat(testFoto.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testFoto.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testFoto.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
    }

    @Test
    public void getAllFotos() throws Exception {
        // Initialize the database
        fotoRepository.save(foto);

        // Get all the fotos
        restFotoMockMvc.perform(get("/api/fotos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(foto.getId())))
                .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
                .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))));
    }

    @Test
    public void getFoto() throws Exception {
        // Initialize the database
        fotoRepository.save(foto);

        // Get the foto
        restFotoMockMvc.perform(get("/api/fotos/{id}", foto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(foto.getId()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)));
    }

    @Test
    public void getNonExistingFoto() throws Exception {
        // Get the foto
        restFotoMockMvc.perform(get("/api/fotos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateFoto() throws Exception {
        // Initialize the database
        fotoRepository.save(foto);

		int databaseSizeBeforeUpdate = fotoRepository.findAll().size();

        // Update the foto
        foto.setCodigo(UPDATED_CODIGO);
        foto.setData(UPDATED_DATA);
        foto.setDataContentType(UPDATED_DATA_CONTENT_TYPE);

        restFotoMockMvc.perform(put("/api/fotos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(foto)))
                .andExpect(status().isOk());

        // Validate the Foto in the database
        List<Foto> fotos = fotoRepository.findAll();
        assertThat(fotos).hasSize(databaseSizeBeforeUpdate);
        Foto testFoto = fotos.get(fotos.size() - 1);
        assertThat(testFoto.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testFoto.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testFoto.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
    }

    @Test
    public void deleteFoto() throws Exception {
        // Initialize the database
        fotoRepository.save(foto);

		int databaseSizeBeforeDelete = fotoRepository.findAll().size();

        // Get the foto
        restFotoMockMvc.perform(delete("/api/fotos/{id}", foto.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Foto> fotos = fotoRepository.findAll();
        assertThat(fotos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
