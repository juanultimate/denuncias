package com.denuncias.web.rest;

import com.denuncias.Application;
import com.denuncias.domain.Denuncia;
import com.denuncias.repository.DenunciaRepository;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.denuncias.domain.enumeration.Estado;

/**
 * Test class for the DenunciaResource REST controller.
 *
 * @see DenunciaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DenunciaResourceIntTest {

    private static final String DEFAULT_CODIGO = "AAAAA";
    private static final String UPDATED_CODIGO = "BBBBB";

    private static final LocalDateTime DEFAULT_FECHA = LocalDateTime.ofEpochSecond(0L,0, ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_FECHA = LocalDateTime.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_SANCIONABLE = false;
    private static final Boolean UPDATED_SANCIONABLE = true;
    private static final String DEFAULT_LATITUD = "AAAAA";
    private static final String UPDATED_LATITUD = "BBBBB";
    private static final String DEFAULT_LONGITUD = "AAAAA";
    private static final String UPDATED_LONGITUD = "BBBBB";
    private static final String DEFAULT_PLACA = "AAAAA";
    private static final String UPDATED_PLACA = "BBBBB";

    private static final Estado DEFAULT_ESTADO = Estado.Creada;
    private static final Estado UPDATED_ESTADO = Estado.Enviada;

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    @Inject
    private DenunciaRepository denunciaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDenunciaMockMvc;

    private Denuncia denuncia;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DenunciaResource denunciaResource = new DenunciaResource();
        ReflectionTestUtils.setField(denunciaResource, "denunciaRepository", denunciaRepository);
        this.restDenunciaMockMvc = MockMvcBuilders.standaloneSetup(denunciaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        denunciaRepository.deleteAll();
        denuncia = new Denuncia();
        denuncia.setCodigo(DEFAULT_CODIGO);
        denuncia.setFecha(DEFAULT_FECHA);
        denuncia.setSancionable(DEFAULT_SANCIONABLE);
        denuncia.setLatitud(DEFAULT_LATITUD);
        denuncia.setLongitud(DEFAULT_LONGITUD);
        denuncia.setPlaca(DEFAULT_PLACA);
        denuncia.setEstado(DEFAULT_ESTADO);
        denuncia.setFoto(DEFAULT_FOTO);
        denuncia.setFotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    public void createDenuncia() throws Exception {
        int databaseSizeBeforeCreate = denunciaRepository.findAll().size();

        // Create the Denuncia

        restDenunciaMockMvc.perform(post("/api/denuncias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(denuncia)))
                .andExpect(status().isCreated());

        // Validate the Denuncia in the database
        List<Denuncia> denuncias = denunciaRepository.findAll();
        assertThat(denuncias).hasSize(databaseSizeBeforeCreate + 1);
        Denuncia testDenuncia = denuncias.get(denuncias.size() - 1);
        assertThat(testDenuncia.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDenuncia.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testDenuncia.getSancionable()).isEqualTo(DEFAULT_SANCIONABLE);
        assertThat(testDenuncia.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testDenuncia.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testDenuncia.getPlaca()).isEqualTo(DEFAULT_PLACA);
        assertThat(testDenuncia.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testDenuncia.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testDenuncia.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    public void getAllDenuncias() throws Exception {
        // Initialize the database
        denunciaRepository.save(denuncia);

        // Get all the denuncias
        restDenunciaMockMvc.perform(get("/api/denuncias?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(denuncia.getId())))
                .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
                .andExpect(jsonPath("$.[*].sancionable").value(hasItem(DEFAULT_SANCIONABLE.booleanValue())))
                .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.toString())))
                .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.toString())))
                .andExpect(jsonPath("$.[*].placa").value(hasItem(DEFAULT_PLACA.toString())))
                .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
                .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))));
    }

    @Test
    public void getDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.save(denuncia);

        // Get the denuncia
        restDenunciaMockMvc.perform(get("/api/denuncias/{id}", denuncia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(denuncia.getId()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.sancionable").value(DEFAULT_SANCIONABLE.booleanValue()))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.toString()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.toString()))
            .andExpect(jsonPath("$.placa").value(DEFAULT_PLACA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)));
    }

    @Test
    public void getNonExistingDenuncia() throws Exception {
        // Get the denuncia
        restDenunciaMockMvc.perform(get("/api/denuncias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.save(denuncia);

		int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();

        // Update the denuncia
        denuncia.setCodigo(UPDATED_CODIGO);
        denuncia.setFecha(UPDATED_FECHA);
        denuncia.setSancionable(UPDATED_SANCIONABLE);
        denuncia.setLatitud(UPDATED_LATITUD);
        denuncia.setLongitud(UPDATED_LONGITUD);
        denuncia.setPlaca(UPDATED_PLACA);
        denuncia.setEstado(UPDATED_ESTADO);
        denuncia.setFoto(UPDATED_FOTO);
        denuncia.setFotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restDenunciaMockMvc.perform(put("/api/denuncias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(denuncia)))
                .andExpect(status().isOk());

        // Validate the Denuncia in the database
        List<Denuncia> denuncias = denunciaRepository.findAll();
        assertThat(denuncias).hasSize(databaseSizeBeforeUpdate);
        Denuncia testDenuncia = denuncias.get(denuncias.size() - 1);
        assertThat(testDenuncia.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDenuncia.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testDenuncia.getSancionable()).isEqualTo(UPDATED_SANCIONABLE);
        assertThat(testDenuncia.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testDenuncia.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testDenuncia.getPlaca()).isEqualTo(UPDATED_PLACA);
        assertThat(testDenuncia.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testDenuncia.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testDenuncia.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    public void deleteDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.save(denuncia);

		int databaseSizeBeforeDelete = denunciaRepository.findAll().size();

        // Get the denuncia
        restDenunciaMockMvc.perform(delete("/api/denuncias/{id}", denuncia.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Denuncia> denuncias = denunciaRepository.findAll();
        assertThat(denuncias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
