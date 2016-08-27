package com.denuncias.service.denuncias;

import com.denuncias.repository.DenunciaRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CodeGenerator {

    @Inject
    private DenunciaRepository denunciaRepository;
    public String generarCodigo(){
        return "AME".concat(String.valueOf(denunciaRepository.count()));
    }
}
