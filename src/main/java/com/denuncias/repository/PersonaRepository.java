package com.denuncias.repository;

import com.denuncias.domain.Persona;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Persona entity.
 */
public interface PersonaRepository extends MongoRepository<Persona,String> {

}
