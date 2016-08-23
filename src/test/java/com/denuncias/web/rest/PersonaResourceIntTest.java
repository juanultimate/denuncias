package com.denuncias.web.rest;

import com.denuncias.Application;
import com.denuncias.domain.Persona;
import com.denuncias.repository.PersonaRepository;

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
 * Test class for the PersonaResource REST controller.
 *
 * @see PersonaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PersonaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final Integer DEFAULT_EDAD = 1;
    private static final Integer UPDATED_EDAD = 2;

    @Inject
    private PersonaRepository personaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonaMockMvc;

    private Persona persona;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonaResource personaResource = new PersonaResource();
        ReflectionTestUtils.setField(personaResource, "personaRepository", personaRepository);
        this.restPersonaMockMvc = MockMvcBuilders.standaloneSetup(personaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personaRepository.deleteAll();
        persona = new Persona();
        persona.setNombre(DEFAULT_NOMBRE);
        persona.setEdad(DEFAULT_EDAD);
    }

    @Test
    public void createPersona() throws Exception {
        int databaseSizeBeforeCreate = personaRepository.findAll().size();

        // Create the Persona

        restPersonaMockMvc.perform(post("/api/personas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(persona)))
                .andExpect(status().isCreated());

        // Validate the Persona in the database
        List<Persona> personas = personaRepository.findAll();
        assertThat(personas).hasSize(databaseSizeBeforeCreate + 1);
        Persona testPersona = personas.get(personas.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersona.getEdad()).isEqualTo(DEFAULT_EDAD);
    }

    @Test
    public void getAllPersonas() throws Exception {
        // Initialize the database
        personaRepository.save(persona);

        // Get all the personas
        restPersonaMockMvc.perform(get("/api/personas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)));
    }

    @Test
    public void getPersona() throws Exception {
        // Initialize the database
        personaRepository.save(persona);

        // Get the persona
        restPersonaMockMvc.perform(get("/api/personas/{id}", persona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(persona.getId()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD));
    }

    @Test
    public void getNonExistingPersona() throws Exception {
        // Get the persona
        restPersonaMockMvc.perform(get("/api/personas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePersona() throws Exception {
        // Initialize the database
        personaRepository.save(persona);

		int databaseSizeBeforeUpdate = personaRepository.findAll().size();

        // Update the persona
        persona.setNombre(UPDATED_NOMBRE);
        persona.setEdad(UPDATED_EDAD);

        restPersonaMockMvc.perform(put("/api/personas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(persona)))
                .andExpect(status().isOk());

        // Validate the Persona in the database
        List<Persona> personas = personaRepository.findAll();
        assertThat(personas).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personas.get(personas.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersona.getEdad()).isEqualTo(UPDATED_EDAD);
    }

    @Test
    public void deletePersona() throws Exception {
        // Initialize the database
        personaRepository.save(persona);

		int databaseSizeBeforeDelete = personaRepository.findAll().size();

        // Get the persona
        restPersonaMockMvc.perform(delete("/api/personas/{id}", persona.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Persona> personas = personaRepository.findAll();
        assertThat(personas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
