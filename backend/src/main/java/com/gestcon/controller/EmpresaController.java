package com.gestcon.controller;

import com.gestcon.model.Empresa;
import com.gestcon.repository.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public List<Empresa> getAllEmpresas() {
        return empresaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaRepository.findById(id);
        return empresa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Empresa createEmpresa(@Valid @RequestBody Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable Long id, @Valid @RequestBody Empresa empresaDetails) {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(id);
        if (!optionalEmpresa.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresa = optionalEmpresa.get();
        empresa.setNome(empresaDetails.getNome());
        empresa.setCnpj(empresaDetails.getCnpj());
        empresa.setEndereco(empresaDetails.getEndereco());

        Empresa updatedEmpresa = empresaRepository.save(empresa);
        return ResponseEntity.ok(updatedEmpresa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaRepository.findById(id);
        if (!empresa.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        empresaRepository.delete(empresa.get());
        return ResponseEntity.noContent().build();
    }
}
