package com.gestcon.controller;

import com.gestcon.model.Pagamento;
import com.gestcon.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para gerenciamento de pagamentos.
 * Herda operações CRUD básicas da classe BaseController e adiciona operações específicas.
 */
@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController extends BaseController<Pagamento, Long> {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Override
    protected JpaRepository<Pagamento, Long> getRepository() {
        return pagamentoRepository;
    }

    @Override
    protected void updateEntity(Pagamento existingPagamento, Pagamento pagamentoDetails) {
        existingPagamento.setNumeroParcela(pagamentoDetails.getNumeroParcela());
        existingPagamento.setValorPrevisto(pagamentoDetails.getValorPrevisto());
        existingPagamento.setValorPago(pagamentoDetails.getValorPago());
        existingPagamento.setDataVencimento(pagamentoDetails.getDataVencimento());
        existingPagamento.setDataPagamento(pagamentoDetails.getDataPagamento());
        existingPagamento.setStatus(pagamentoDetails.getStatus());
        existingPagamento.setObservacoes(pagamentoDetails.getObservacoes());
        existingPagamento.setNumeroNotaFiscal(pagamentoDetails.getNumeroNotaFiscal());
        existingPagamento.setDocumentoComprobatorio(pagamentoDetails.getDocumentoComprobatorio());
        existingPagamento.setUsuarioResponsavel(pagamentoDetails.getUsuarioResponsavel());
    }

    /**
     * Busca pagamentos por contrato.
     */
    @GetMapping("/contrato/{contratoId}")
    public List<Pagamento> getPagamentosByContrato(@PathVariable Long contratoId) {
        return pagamentoRepository.findByContratoId(contratoId);
    }

    /**
     * Busca pagamentos por status.
     */
    @GetMapping("/status/{status}")
    public List<Pagamento> getPagamentosByStatus(@PathVariable String status) {
        return pagamentoRepository.findByStatus(status);
    }

    /**
     * Busca pagamentos atrasados.
     */
    @GetMapping("/atrasados")
    public List<Pagamento> getPagamentosAtrasados() {
        return pagamentoRepository.findPagamentosAtrasados(LocalDate.now());
    }

    /**
     * Busca pagamentos por usuário responsável.
     */
    @GetMapping("/responsavel/{usuario}")
    public List<Pagamento> getPagamentosByResponsavel(@PathVariable String usuario) {
        return pagamentoRepository.findByUsuarioResponsavel(usuario);
    }

    /**
     * Busca pagamentos com vencimento próximo (próximos 7 dias).
     */
    @GetMapping("/vencimento-proximo")
    public List<Pagamento> getPagamentosVencimentoProximo() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(7);
        return pagamentoRepository.findPagamentosVencimentoProximo(hoje, limite);
    }

    /**
     * Marca um pagamento como pago.
     */
    @PutMapping("/{id}/marcar-pago")
    public ResponseEntity<Pagamento> marcarComoPago(@PathVariable Long id,
                                                   @RequestParam LocalDate dataPagamento) {
        return pagamentoRepository.findById(id)
                .map(pagamento -> {
                    pagamento.setStatus("PAGO");
                    pagamento.setDataPagamento(dataPagamento);
                    return ResponseEntity.ok(pagamentoRepository.save(pagamento));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Calcula a diferença entre valor previsto e pago.
     */
    @GetMapping("/{id}/diferenca")
    public ResponseEntity<String> calcularDiferenca(@PathVariable Long id) {
        return pagamentoRepository.findById(id)
                .map(pagamento -> {
                    String diferenca = pagamento.calcularDiferenca().toString();
                    return ResponseEntity.ok("Diferença: R$ " + diferenca);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
