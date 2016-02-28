package com.denuncias.domain;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

import com.denuncias.domain.enumeration.Estado;

/**
 * A Denuncia.
 */

@Document(collection = "denuncia")
public class Denuncia implements Serializable {

    @Id
    private String id;

    @Field("codigo")
    private String codigo;
    
    @Field("fecha")
    private LocalDate fecha;
    
    @Field("sancionable")
    private Boolean sancionable;
    
    @Field("latitud")
    private String latitud;
    
    @Field("longitud")
    private String longitud;
    
    @Field("placa")
    private String placa;
    
    @Field("estado")
    private Estado estado;
    
    @Field("foto")
    private byte[] foto;
    
    @Field("foto_content_type")
    private String fotoContentType;
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

    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getSancionable() {
        return sancionable;
    }
    
    public void setSancionable(Boolean sancionable) {
        this.sancionable = sancionable;
    }

    public String getLatitud() {
        return latitud;
    }
    
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }
    
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public byte[] getFoto() {
        return foto;
    }
    
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Denuncia denuncia = (Denuncia) o;
        if(denuncia.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, denuncia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Denuncia{" +
            "id=" + id +
            ", codigo='" + codigo + "'" +
            ", fecha='" + fecha + "'" +
            ", sancionable='" + sancionable + "'" +
            ", latitud='" + latitud + "'" +
            ", longitud='" + longitud + "'" +
            ", placa='" + placa + "'" +
            ", estado='" + estado + "'" +
            ", foto='" + foto + "'" +
            ", fotoContentType='" + fotoContentType + "'" +
            '}';
    }
}
