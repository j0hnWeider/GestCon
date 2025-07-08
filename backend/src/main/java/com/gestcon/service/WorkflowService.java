package com.gestcon.service;

import com.gestcon.model.Contrato;
import com.gestcon.model.ProcessoContrato;
import com.gestcon.repository.ProcessoContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Serviço responsável por gerenciar o workflow e mudanças de status dos contratos.
 * Implementa a lógica de negócio para transições de estado e auditoria.
 */
@Service
@Transactional
public class WorkflowService {

    @Autowired
    private ProcessoContratoRepository processoContratoRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    // Mapa de transições válidas de status
    private static final Map<String, List<String>> TRANSICOES_VALIDAS = new HashMap<>();
    
    static {
        TRANSICOES_VALIDAS.put("RASCUNHO", List.of("EM_ANALISE", "CANCELADO"));
        TRANSICOES_VALIDAS.put("EM_ANALISE", List.of("APROVADO", "REJEITADO", "PENDENTE_DOCUMENTOS"));
        TRANSICOES_VALIDAS.put("PENDENTE_DOCUMENTOS", List.of("EM_ANALISE", "CANCELADO"));
        TRANSICOES_VALIDAS.put("APROVADO", List.of("ASSINADO", "REJEITADO"));
        TRANSICOES_VALIDAS.put("ASSINADO", List.of("ATIVO", "CANCELADO"));
        TRANSICOES_VALIDAS.put("ATIVO", List.of("EM_RENOVACAO", "SUSPENSO", "ENCERRADO"));
        TRANSICOES_VALIDAS.put("EM_RENOVACAO", List.of("ATIVO", "ENCERRADO"));
        TRANSICOES_VALIDAS.put("SUSPENSO", List.of("ATIVO", "ENCERRADO"));
        TRANSICOES_VALIDAS.put("INADIMPLENTE", List.of("ATIVO", "ENCERRADO"));
        TRANSICOES_VALIDAS.put("REJEITADO", List.of("RASCUNHO", "CANCELADO"));
        TRANSICOES_VALIDAS.put("ENCERRADO", List.of()); // Estado final
        TRANSICOES_VALIDAS.put("CANCELADO", List.of()); // Estado final
    }

    /**
     * Altera o status de um contrato seguindo as regras de workflow.
     */
    public ProcessoContrato alterarStatus(Contrato contrato, String novoStatus, 
                                        String acaoRealizada, String usuarioResponsavel, 
                                        String observacoes) {
        
        String statusAtual = contrato.getStatus();
        
        // Validar se a transição é permitida
        if (!isTransicaoValida(statusAtual, novoStatus)) {
            throw new IllegalStateException(
                String.format("Transição de '%s' para '%s' não é permitida", statusAtual, novoStatus)
            );
        }

        // Criar registro do processo
        ProcessoContrato processo = new ProcessoContrato(
            contrato, statusAtual, novoStatus, acaoRealizada, usuarioResponsavel
        );
        processo.setObservacoes(observacoes);

        // Atualizar status do contrato
        contrato.setStatus(novoStatus);

        // Salvar processo
        ProcessoContrato processoSalvo = processoContratoRepository.save(processo);

        // Enviar notificações baseadas no novo status
        enviarNotificacoesPorStatus(contrato, novoStatus, usuarioResponsavel);

        return processoSalvo;
    }

    /**
     * Verifica se uma transição de status é válida.
     */
    public boolean isTransicaoValida(String statusAtual, String novoStatus) {
        if (statusAtual == null || novoStatus == null) {
            return false;
        }
        
        List<String> transicoesPermitidas = TRANSICOES_VALIDAS.get(statusAtual);
        return transicoesPermitidas != null && transicoesPermitidas.contains(novoStatus);
    }

    /**
     * Obtém o histórico completo de processos de um contrato.
     */
    public List<ProcessoContrato> obterHistoricoContrato(Long contratoId) {
        return processoContratoRepository.findByContratoIdOrderByDataAcaoDesc(contratoId);
    }

    /**
     * Obtém os próximos status possíveis para um contrato.
     */
    public List<String> obterProximosStatusPossiveis(String statusAtual) {
        return TRANSICOES_VALIDAS.getOrDefault(statusAtual, List.of());
    }

    /**
     * Verifica se um contrato está em estado final.
     */
    public boolean isStatusFinal(String status) {
        List<String> proximosStatus = TRANSICOES_VALIDAS.get(status);
        return proximosStatus == null || proximosStatus.isEmpty();
    }

    /**
     * Envia notificações baseadas no novo status do contrato.
     */
    private void enviarNotificacoesPorStatus(Contrato contrato, String novoStatus, String usuarioResponsavel) {
        switch (novoStatus) {
            case "EM_ANALISE":
                notificacaoService.notificarAnaliseContrato(contrato, usuarioResponsavel);
                break;
            case "APROVADO":
                notificacaoService.notificarAprovacaoContrato(contrato, usuarioResponsavel);
                break;
            case "REJEITADO":
                notificacaoService.notificarRejeicaoContrato(contrato, usuarioResponsavel);
                break;
            case "ATIVO":
                notificacaoService.notificarContratoAtivo(contrato, usuarioResponsavel);
                break;
            case "VENCENDO":
                notificacaoService.notificarContratoVencendo(contrato, usuarioResponsavel);
                break;
            case "ENCERRADO":
                notificacaoService.notificarContratoEncerrado(contrato, usuarioResponsavel);
                break;
            case "INADIMPLENTE":
                notificacaoService.notificarContratoInadimplente(contrato, usuarioResponsavel);
                break;
        }
    }

    /**
     * Processa automaticamente contratos que precisam de mudança de status.
     * Método para ser executado por scheduler.
     */
    @Transactional
    public void processarStatusAutomaticos() {
        // Implementar lógica para detectar contratos que precisam de mudança automática
        // Ex: contratos vencendo, inadimplentes, etc.
        
        // Esta funcionalidade seria expandida com queries específicas
        // e regras de negócio mais complexas
    }

    /**
     * Adiciona um documento ao processo.
     */
    public void adicionarDocumentoProcesso(Long processoId, String caminhoDocumento) {
        ProcessoContrato processo = processoContratoRepository.findById(processoId)
            .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
        
        processo.setDocumentoAnexo(caminhoDocumento);
        processoContratoRepository.save(processo);
    }

    /**
     * Obtém estatísticas de workflow para dashboard.
     */
    public Map<String, Long> obterEstatisticasWorkflow() {
        Map<String, Long> estatisticas = new HashMap<>();
        
        // Implementar queries para contar contratos por status
        // Esta seria uma funcionalidade expandida com queries específicas
        
        return estatisticas;
    }
}
