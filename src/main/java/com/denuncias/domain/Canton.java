package com.denuncias.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Canton.
 */

@Document(collection = "canton")
public class Canton implements Serializable {
    @JsonCreator
    public Canton(@JsonProperty("id") String id, @JsonProperty("codigo") String codigo, @JsonProperty("nombre") String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Canton() {
    }


    @JsonCreator
    public Canton( String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Canton newCanton=mapper.readValue(json,Canton.class);
            this.id = newCanton.id;
            this.codigo = newCanton.codigo;
            this.nombre =newCanton.nombre;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    @Id
    private String id;

    @Field("codigo")
    private String codigo;

    @Field("nombre")
    private String nombre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Canton canton = (Canton) o;
        if(canton.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, canton.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Canton{" +
            "id=" + id +
            ", codigo='" + codigo + "'" +
            ", nombre='" + nombre + "'" +
            '}';
    }


}
