package com.denuncias.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.denuncias.domain.enumeration.Estado;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    //2011-12-03T10:15:30+01:00
    private ZonedDateTime fecha;

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

    @Field("direccion")
    private String direccion;

    @Field("pagado")
    private Boolean pagado;

    @DBRef
    private Canton canton;

    @DBRef
    private User usuarioOperador;

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

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public void setFecha(ZonedDateTime fecha) {
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

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public User getUsuarioOperador() {
        return usuarioOperador;
    }

    public void setUsuarioOperador(User usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
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
            //", canton='"+canton.toString()+
            ", direccion='" + direccion + "'" +
            ", pagado='" + pagado + "'" +
            ", operador='" + usuarioOperador + "'" +
            '}';
    }



}
