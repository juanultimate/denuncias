package com.denuncias.repository;

import com.denuncias.domain.Denuncia;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Denuncia entity.
 */
public interface DenunciaRepository extends MongoRepository<Denuncia,String> {

}
