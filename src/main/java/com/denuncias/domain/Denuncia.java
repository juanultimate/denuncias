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

    @Field("canton")
    private String canton;

    @Field("fecha")
    private LocalDate fecha;

    @Field("sancion")
    private Boolean sancion;

    @Field("estado")
    private Estado estado;

    @Field("distrito")
    private String distrito;

    @Field("tipo_sancion")
    private String tipoSancion;

    @Field("placa")
    private String placa;

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

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getSancion() {
        return sancion;
    }

    public void setSancion(Boolean sancion) {
        this.sancion = sancion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getTipoSancion() {
        return tipoSancion;
    }

    public void setTipoSancion(String tipoSancion) {
        this.tipoSancion = tipoSancion;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
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
            ", canton='" + canton + "'" +
            ", fecha='" + fecha + "'" +
            ", sancion='" + sancion + "'" +
            ", estado='" + estado + "'" +
            ", distrito='" + distrito + "'" +
            ", tipoSancion='" + tipoSancion + "'" +
            ", placa='" + placa + "'" +
            '}';
    }
}
