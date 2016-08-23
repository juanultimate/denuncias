package com.denuncias.domain;

import java.io.Serializable;

/**
 * Created by JuanGabriel on 23/3/2016.
 */
public class ReportData implements Serializable {
    String label;
    Long value;
    public ReportData(String label, Long value) {
        this.label = label;
        this.value = value;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }




}
