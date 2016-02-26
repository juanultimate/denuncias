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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.denuncias.domain.enumeration.Com.denuncias.domain.Estado;

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
    private static final String DEFAULT_CANTON = "AAAAA";
    private static final String UPDATED_CANTON = "BBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_SANCION = false;
    private static final Boolean UPDATED_SANCION = true;
    
    private static final Com.denuncias.domain.Estado DEFAULT_ESTADO = Com.denuncias.domain.Estado.CREADA;
    private static final Com.denuncias.domain.Estado UPDATED_ESTADO = Com.denuncias.domain.Estado.ENVIADA;
    private static final String DEFAULT_DISTRITO = "AAAAA";
    private static final String UPDATED_DISTRITO = "BBBBB";
    private static final String DEFAULT_TIPO_SANCION = "AAAAA";
    private static final String UPDATED_TIPO_SANCION = "BBBBB";
    private static final String DEFAULT_PLACA = "AAAAA";
    private static final String UPDATED_PLACA = "BBBBB";

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
        denuncia.setCanton(DEFAULT_CANTON);
        denuncia.setFecha(DEFAULT_FECHA);
        denuncia.setSancion(DEFAULT_SANCION);
        denuncia.setEstado(DEFAULT_ESTADO);
        denuncia.setDistrito(DEFAULT_DISTRITO);
        denuncia.setTipoSancion(DEFAULT_TIPO_SANCION);
        denuncia.setPlaca(DEFAULT_PLACA);
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
        assertThat(testDenuncia.getCanton()).isEqualTo(DEFAULT_CANTON);
        assertThat(testDenuncia.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testDenuncia.getSancion()).isEqualTo(DEFAULT_SANCION);
        assertThat(testDenuncia.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testDenuncia.getDistrito()).isEqualTo(DEFAULT_DISTRITO);
        assertThat(testDenuncia.getTipoSancion()).isEqualTo(DEFAULT_TIPO_SANCION);
        assertThat(testDenuncia.getPlaca()).isEqualTo(DEFAULT_PLACA);
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
                .andExpect(jsonPath("$.[*].canton").value(hasItem(DEFAULT_CANTON.toString())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
                .andExpect(jsonPath("$.[*].sancion").value(hasItem(DEFAULT_SANCION.booleanValue())))
                .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
                .andExpect(jsonPath("$.[*].distrito").value(hasItem(DEFAULT_DISTRITO.toString())))
                .andExpect(jsonPath("$.[*].tipoSancion").value(hasItem(DEFAULT_TIPO_SANCION.toString())))
                .andExpect(jsonPath("$.[*].placa").value(hasItem(DEFAULT_PLACA.toString())));
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
            .andExpect(jsonPath("$.canton").value(DEFAULT_CANTON.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.sancion").value(DEFAULT_SANCION.booleanValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.distrito").value(DEFAULT_DISTRITO.toString()))
            .andExpect(jsonPath("$.tipoSancion").value(DEFAULT_TIPO_SANCION.toString()))
            .andExpect(jsonPath("$.placa").value(DEFAULT_PLACA.toString()));
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
        denuncia.setCanton(UPDATED_CANTON);
        denuncia.setFecha(UPDATED_FECHA);
        denuncia.setSancion(UPDATED_SANCION);
        denuncia.setEstado(UPDATED_ESTADO);
        denuncia.setDistrito(UPDATED_DISTRITO);
        denuncia.setTipoSancion(UPDATED_TIPO_SANCION);
        denuncia.setPlaca(UPDATED_PLACA);

        restDenunciaMockMvc.perform(put("/api/denuncias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(denuncia)))
                .andExpect(status().isOk());

        // Validate the Denuncia in the database
        List<Denuncia> denuncias = denunciaRepository.findAll();
        assertThat(denuncias).hasSize(databaseSizeBeforeUpdate);
        Denuncia testDenuncia = denuncias.get(denuncias.size() - 1);
        assertThat(testDenuncia.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDenuncia.getCanton()).isEqualTo(UPDATED_CANTON);
        assertThat(testDenuncia.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testDenuncia.getSancion()).isEqualTo(UPDATED_SANCION);
        assertThat(testDenuncia.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testDenuncia.getDistrito()).isEqualTo(UPDATED_DISTRITO);
        assertThat(testDenuncia.getTipoSancion()).isEqualTo(UPDATED_TIPO_SANCION);
        assertThat(testDenuncia.getPlaca()).isEqualTo(UPDATED_PLACA);
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
