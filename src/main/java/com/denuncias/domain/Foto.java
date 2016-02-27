package com.denuncias.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Foto.
 */

@Document(collection = "foto")
public class Foto implements Serializable {

    @Id
    private String id;

    @Field("codigo")
    private String codigo;
    
    @Field("data")
    private byte[] data;
    
    @Field("data_content_type")
    private String dataContentType;
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

    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Foto foto = (Foto) o;
        if(foto.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, foto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Foto{" +
            "id=" + id +
            ", codigo='" + codigo + "'" +
            ", data='" + data + "'" +
            ", dataContentType='" + dataContentType + "'" +
            '}';
    }
}
