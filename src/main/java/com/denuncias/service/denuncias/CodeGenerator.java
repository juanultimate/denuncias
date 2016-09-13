package com.denuncias.service.denuncias;

import com.denuncias.repository.DenunciaRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class CodeGenerator {

    @Inject
    private DenunciaRepository denunciaRepository;
    public String generarCodigo(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        StringBuilder codigo = new StringBuilder("ACT-");
        codigo.append(year).append("-").append(String.format("%02d", month)).append("-").append(String.format("%03d", denunciaRepository.count()));
        return codigo.toString();
    }
}
