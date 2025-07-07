package com.gestcon.controller;

import com.gestcon.model.Contrato;
import com.gestcon.repository.ContratoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contratos")
@CrossOrigin(origins = "*")
public class ContratoController {

    @Autowired
    private ContratoRepository contratoRepository;

    @GetMapping
    public List<Contrato> getAllContratos() {
        return contratoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> getContratoById(@PathVariable Long id) {
        Optional<Contrato> contrato = contratoRepository.findById(id);
        return contrato.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Contrato createContrato(@Valid @RequestBody Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrato> updateContrato(@PathVariable Long id, @Valid @RequestBody Contrato contratoDetails) {
        Optional<Contrato> optionalContrato = contratoRepository.findById(id);
        if (!optionalContrato.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Contrato contrato = optionalContrato.get();
        contrato.setNumeroContrato(contratoDetails.getNumeroContrato());
        contrato.setEmpresa(contratoDetails.getEmpresa());
        contrato.setObjeto(contratoDetails.getObjeto());
        contrato.setVigenciaInicio(contratoDetails.getVigenciaInicio());
        contrato.setVigenciaFim(contratoDetails.getVigenciaFim());
        contrato.setValorTotal(contratoDetails.getValorTotal());
        contrato.setStatus(contratoDetails.getStatus());
        contrato.setResponsavel(contratoDetails.getResponsavel());

        Contrato updatedContrato = contratoRepository.save(contrato);
        return ResponseEntity.ok(updatedContrato);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContrato(@PathVariable Long id) {
        Optional<Contrato> contrato = contratoRepository.findById(id);
        if (!contrato.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        contratoRepository.delete(contrato.get());
        return ResponseEntity.noContent().build();
    }
}
