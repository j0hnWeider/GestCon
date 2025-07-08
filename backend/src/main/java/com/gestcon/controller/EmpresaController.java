package com.gestcon.controller;

import com.gestcon.model.Empresa;
import com.gestcon.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciamento de empresas.
 * Herda operações CRUD básicas da classe BaseController.
 */
@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController extends BaseController<Empresa, Long> {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    protected JpaRepository<Empresa, Long> getRepository() {
        return empresaRepository;
    }

    @Override
    protected void updateEntity(Empresa existingEmpresa, Empresa empresaDetails) {
        existingEmpresa.setNome(empresaDetails.getNome());
        existingEmpresa.setCnpj(empresaDetails.getCnpj());
        existingEmpresa.setEndereco(empresaDetails.getEndereco());
    }
}
