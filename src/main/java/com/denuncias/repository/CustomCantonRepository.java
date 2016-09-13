package com.denuncias.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CustomCantonRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<String> getDistinctProvincias(){
        return mongoTemplate.getCollection("canton").distinct("provincia");
    }
}
