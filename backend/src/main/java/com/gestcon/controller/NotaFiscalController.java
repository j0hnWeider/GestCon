package com.gestcon.controller;

import com.gestcon.model.NotaFiscal;
import com.gestcon.repository.NotaFiscalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para gerenciamento de notas fiscais.
 * Herda operações CRUD básicas da classe BaseController e adiciona operações específicas.
 */
@RestController
@RequestMapping("/api/notas-fiscais")
@CrossOrigin(origins = "*")
public class NotaFiscalController extends BaseController<NotaFiscal, Long> {

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    @Override
    protected JpaRepository<NotaFiscal, Long> getRepository() {
        return notaFiscalRepository;
    }

    @Override
    protected void updateEntity(NotaFiscal existingNotaFiscal, NotaFiscal notaFiscalDetails) {
        existingNotaFiscal.setNumeroNota(notaFiscalDetails.getNumeroNota());
        existingNotaFiscal.setSerie(notaFiscalDetails.getSerie());
        existingNotaFiscal.setValorTotal(notaFiscalDetails.getValorTotal());
        existingNotaFiscal.setValorImpostos(notaFiscalDetails.getValorImpostos());
        existingNotaFiscal.setDataEmissao(notaFiscalDetails.getDataEmissao());
        existingNotaFiscal.setDataVencimento(notaFiscalDetails.getDataVencimento());
        existingNotaFiscal.setStatus(notaFiscalDetails.getStatus());
        existingNotaFiscal.setDescricaoServicos(notaFiscalDetails.getDescricaoServicos());
        existingNotaFiscal.setObservacoes(notaFiscalDetails.getObservacoes());
        existingNotaFiscal.setArquivoPdf(notaFiscalDetails.getArquivoPdf());
        existingNotaFiscal.setArquivoXml(notaFiscalDetails.getArquivoXml());
        existingNotaFiscal.setChaveAcesso(notaFiscalDetails.getChaveAcesso());
    }

    /**
     * Busca notas fiscais por contrato.
     */
    @GetMapping("/contrato/{contratoId}")
    public List<NotaFiscal> getNotasFiscaisByContrato(@PathVariable Long contratoId) {
        return notaFiscalRepository.findByContratoId(contratoId);
    }

    /**
     * Busca notas fiscais por status.
     */
    @GetMapping("/status/{status}")
    public List<NotaFiscal> getNotasFiscaisByStatus(@PathVariable String status) {
        return notaFiscalRepository.findByStatus(status);
    }

    /**
     * Busca notas fiscais vencidas.
     */
    @GetMapping("/vencidas")
    public List<NotaFiscal> getNotasFiscaisVencidas() {
        return notaFiscalRepository.findNotasVencidas(LocalDate.now());
    }

    /**
     * Aprova uma nota fiscal.
     */
    @PutMapping("/{id}/aprovar")
    public ResponseEntity<NotaFiscal> aprovarNotaFiscal(@PathVariable Long id, 
                                                       @RequestParam String usuarioAprovacao) {
        return notaFiscalRepository.findById(id)
                .map(notaFiscal -> {
                    notaFiscal.aprovar(usuarioAprovacao);
                    return ResponseEntity.ok(notaFiscalRepository.save(notaFiscal));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Rejeita uma nota fiscal.
     */
    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<NotaFiscal> rejeitarNotaFiscal(@PathVariable Long id, 
                                                        @RequestParam String usuarioAprovacao,
                                                        @RequestParam String motivo) {
        return notaFiscalRepository.findById(id)
                .map(notaFiscal -> {
                    notaFiscal.rejeitar(usuarioAprovacao, motivo);
                    return ResponseEntity.ok(notaFiscalRepository.save(notaFiscal));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
