package com.denuncias.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TipoSancion.
 */

@Document(collection = "tipo_sancion")
public class TipoSancion implements Serializable {

    @Id
    private String id;

    @NotNull
    @Field("nombre")
    private String nombre;
    
    @NotNull
    @Min(value = 0)
    @Field("costo")
    private Double costo;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getCosto() {
        return costo;
    }
    
    public void setCosto(Double costo) {
        this.costo = costo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoSancion tipoSancion = (TipoSancion) o;
        if(tipoSancion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tipoSancion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TipoSancion{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", costo='" + costo + "'" +
            '}';
    }
}
