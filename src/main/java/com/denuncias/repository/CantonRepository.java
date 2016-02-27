package com.denuncias.repository;

import com.denuncias.domain.Canton;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Canton entity.
 */
public interface CantonRepository extends MongoRepository<Canton,String> {

}
