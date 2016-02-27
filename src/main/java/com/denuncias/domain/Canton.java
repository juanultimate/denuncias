package com.denuncias.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Canton.
 */

@Document(collection = "canton")
public class Canton implements Serializable {

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
