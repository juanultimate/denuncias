package com.denuncias.domain;

import java.io.Serializable;

/**
 * Created by JuanGabriel on 23/3/2016.
 */
public class ReportData implements Serializable {
    String label;
    Integer value;

    public ReportData(String label, Long value) {
        this.label = label;
        value = value;
    }
}
