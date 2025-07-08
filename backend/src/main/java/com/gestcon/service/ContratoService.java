package com.gestcon.service;

import com.gestcon.model.Contrato;
import com.gestcon.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service para regras de negócio relacionadas a contratos.
 * Centraliza a lógica de negócio e operações complexas.
 */
@Service
@Transactional
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    /**
     * Busca todos os contratos.
     */
    public List<Contrato> findAll() {
        return contratoRepository.findAll();
    }

    /**
     * Busca contrato por ID.
     */
    public Optional<Contrato> findById(Long id) {
        return contratoRepository.findById(id);
    }

    /**
     * Salva um contrato.
     */
    public Contrato save(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    /**
     * Remove um contrato.
     */
    public void delete(Long id) {
        contratoRepository.deleteById(id);
    }

    /**
     * Busca contratos por status.
     */
    public List<Contrato> findByStatus(String status) {
        // Implementar quando o método for adicionado ao repository
        return contratoRepository.findAll().stream()
                .filter(contrato -> status.equals(contrato.getStatus()))
                .toList();
    }

    /**
     * Busca contratos próximos ao vencimento.
     */
    public List<Contrato> findContratosProximosVencimento(int dias) {
        LocalDate dataLimite = LocalDate.now().plusDays(dias);
        return contratoRepository.findAll().stream()
                .filter(contrato -> contrato.getVigenciaFim() != null && 
                                  contrato.getVigenciaFim().isBefore(dataLimite) &&
                                  contrato.getVigenciaFim().isAfter(LocalDate.now()))
                .toList();
    }

    /**
     * Busca contratos vencidos.
     */
    public List<Contrato> findContratosVencidos() {
        LocalDate hoje = LocalDate.now();
        return contratoRepository.findAll().stream()
                .filter(contrato -> contrato.getVigenciaFim() != null && 
                                  contrato.getVigenciaFim().isBefore(hoje))
                .toList();
    }

    /**
     * Renova um contrato.
     */
    public Contrato renovarContrato(Long contratoId, LocalDate novaVigenciaFim) {
        Optional<Contrato> contratoOpt = contratoRepository.findById(contratoId);
        if (contratoOpt.isPresent()) {
            Contrato contrato = contratoOpt.get();
            contrato.setVigenciaFim(novaVigenciaFim);
            contrato.setStatus("RENOVADO");
            
            Contrato contratoRenovado = contratoRepository.save(contrato);
            
            // Enviar notificação de renovação
            notificacaoService.notificarContratoAtivo(contratoRenovado, "Sistema");
            
            return contratoRenovado;
        }
        throw new RuntimeException("Contrato não encontrado");
    }

    /**
     * Verifica contratos que precisam de atenção e envia notificações.
     */
    public void verificarContratosParaNotificacao() {
        // Contratos próximos ao vencimento (30 dias)
        List<Contrato> proximosVencimento = findContratosProximosVencimento(30);
        proximosVencimento.forEach(contrato -> 
            notificacaoService.notificarContratoVencendo(contrato, "Sistema"));

        // Contratos vencidos
        List<Contrato> vencidos = findContratosVencidos();
        vencidos.forEach(contrato -> 
            notificacaoService.notificarContratoEncerrado(contrato, "Sistema"));
    }
}
