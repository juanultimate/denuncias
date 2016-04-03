package com.denuncias.domain.enumeration;

/**
 * Created by JuanGabriel on 3/4/2016.
 */
public enum DiaSemana {
    LUNES,MARTES,MIERCOLES,JUEVES,VIERNES,SABADO,DOMINGO;
    public static DiaSemana fromInteger(int x) {
        return DiaSemana.values()[x-1];
    }
}
