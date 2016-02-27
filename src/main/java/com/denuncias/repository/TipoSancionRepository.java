package com.denuncias.repository;

import com.denuncias.domain.TipoSancion;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the TipoSancion entity.
 */
public interface TipoSancionRepository extends MongoRepository<TipoSancion,String> {

}
