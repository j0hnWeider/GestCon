package com.gestcon.controller;

import com.gestcon.model.Contrato;
import com.gestcon.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciamento de contratos.
 * Herda operações CRUD básicas da classe BaseController.
 */
@RestController
@RequestMapping("/api/contratos")
@CrossOrigin(origins = "*")
public class ContratoController extends BaseController<Contrato, Long> {

    @Autowired
    private ContratoRepository contratoRepository;

    @Override
    protected JpaRepository<Contrato, Long> getRepository() {
        return contratoRepository;
    }

    @Override
    protected void updateEntity(Contrato existingContrato, Contrato contratoDetails) {
        existingContrato.setNumeroContrato(contratoDetails.getNumeroContrato());
        existingContrato.setEmpresa(contratoDetails.getEmpresa());
        existingContrato.setObjeto(contratoDetails.getObjeto());
        existingContrato.setVigenciaInicio(contratoDetails.getVigenciaInicio());
        existingContrato.setVigenciaFim(contratoDetails.getVigenciaFim());
        existingContrato.setValorTotal(contratoDetails.getValorTotal());
        existingContrato.setStatus(contratoDetails.getStatus());
        existingContrato.setResponsavel(contratoDetails.getResponsavel());
    }
}
