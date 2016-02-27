package com.denuncias.repository;

import com.denuncias.domain.Foto;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Foto entity.
 */
public interface FotoRepository extends MongoRepository<Foto,String> {

}
