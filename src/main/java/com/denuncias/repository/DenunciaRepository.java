package com.denuncias.repository;

import com.denuncias.domain.Canton;
import com.denuncias.domain.Denuncia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Denuncia entity.
 */
public interface DenunciaRepository extends MongoRepository<Denuncia,String> {
    Page<Denuncia> findByEstadoLike (String estado, Pageable pageable);
    Long countByCanton(Canton canton);
    Long countByPagado(Boolean pagado);





}
