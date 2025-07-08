package com.gestcon.service;

import com.gestcon.model.Contrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

/**
 * Serviço responsável por gerenciar notificações do sistema GestCon.
 * Envia notificações por email e mantém notificações in-app.
 */
@Service
public class NotificacaoService {

    private static final Logger logger = Logger.getLogger(NotificacaoService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Notifica sobre contrato em análise.
     */
    public void notificarAnaliseContrato(Contrato contrato, String usuarioResponsavel) {
        String assunto = "Contrato em Análise - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "O contrato %s da empresa %s está em análise.\n\n" +
            "Responsável: %s\n" +
            "Valor: R$ %s\n" +
            "Vigência: %s a %s",
            contrato.getNumeroContrato(),
            contrato.getEmpresa().getNome(),
            usuarioResponsavel,
            contrato.getValorTotal(),
            contrato.getVigenciaInicio(),
            contrato.getVigenciaFim()
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
        criarNotificacaoInApp(contrato.getId(), "ANALISE", assunto, mensagem);
    }

    /**
     * Notifica sobre aprovação de contrato.
     */
    public void notificarAprovacaoContrato(Contrato contrato, String usuarioResponsavel) {
        String assunto = "Contrato Aprovado - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "O contrato %s foi aprovado com sucesso!\n\n" +
            "Empresa: %s\n" +
            "Aprovado por: %s\n" +
            "Próximo passo: Assinatura do contrato",
            contrato.getNumeroContrato(),
            contrato.getEmpresa().getNome(),
            usuarioResponsavel
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
        criarNotificacaoInApp(contrato.getId(), "APROVACAO", assunto, mensagem);
    }

    /**
     * Notifica sobre rejeição de contrato.
     */
    public void notificarRejeicaoContrato(Contrato contrato, String usuarioResponsavel) {
        String assunto = "Contrato Rejeitado - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "O contrato %s foi rejeitado.\n\n" +
            "Empresa: %s\n" +
            "Rejeitado por: %s\n" +
            "Ação necessária: Revisar documentação e reenviar",
            contrato.getNumeroContrato(),
            contrato.getEmpresa().getNome(),
            usuarioResponsavel
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
        criarNotificacaoInApp(contrato.getId(), "REJEICAO", assunto, mensagem);
    }

    /**
     * Notifica sobre contrato ativo.
     */
    public void notificarContratoAtivo(Contrato contrato, String usuarioResponsavel) {
        String assunto = "Contrato Ativado - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "O contrato %s está agora ATIVO!\n\n" +
            "Empresa: %s\n" +
            "Vigência: %s a %s\n" +
            "Valor Total: R$ %s",
            contrato.getNumeroContrato(),
            contrato.getEmpresa().getNome(),
            contrato.getVigenciaInicio(),
            contrato.getVigenciaFim(),
            contrato.getValorTotal()
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
        criarNotificacaoInApp(contrato.getId(), "ATIVO", assunto, mensagem);
    }

    /**
     * Notifica sobre contrato vencendo.
     */
    public void notificarContratoVencendo(Contrato contrato, String usuarioResponsavel) {
        String assunto = "ALERTA: Contrato Vencendo - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "ATENÇÃO! O contrato %s está próximo do vencimento.\n\n" +
            "Empresa: %s\n" +
            "Data de Vencimento: %s\n" +
            "Ação necessária: Iniciar processo de renovação",
            contrato.getNumeroContrato(),
            contrato.getEmpresa().getNome(),
            contrato.getVigenciaFim()
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
        criarNotificacaoInApp(contrato.getId(), "VENCIMENTO", assunto, mensagem);
    }

    /**
     * Notifica sobre contrato encerrado.
     */
    public void notificarContratoEncerrado(Contrato contrato, String usuarioResponsavel) {
        String assunto = "Contrato Encerrado - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "O contrato %s foi encerrado.\n\n" +
            "Empresa: %s\n" +
            "Encerrado por: %s\n" +
            "Data de Encerramento: %s",
            contrato.getNumeroContrato(),
            contrato.getEmpresa().getNome(),
            usuarioResponsavel,
            java.time.LocalDate.now()
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
        criarNotificacaoInApp(contrato.getId(), "ENCERRAMENTO", assunto, mensagem);
    }

    /**
     * Notifica sobre contrato inadimplente.
     */
    public void notificarContratoInadimplente(Contrato contrato, String usuarioResponsavel) {
        String assunto = "URGENTE: Contrato Inadimplente - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "ALERTA! O contrato %s está INADIMPLENTE.\n\n" +
            "Empresa: %s\n" +
            "Identificado por: %s\n" +
            "Ação necessária: Contatar empresa e verificar pendências",
            contrato.getNumeroContrato(),
            contrato.getEmpresa().getNome(),
            usuarioResponsavel
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
        criarNotificacaoInApp(contrato.getId(), "INADIMPLENCIA", assunto, mensagem);
    }

    /**
     * Envia notificação por email.
     */
    private void enviarEmail(String destinatario, String assunto, String mensagem) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(destinatario);
            email.setSubject(assunto);
            email.setText(mensagem);
            email.setFrom("noreply@gestcon.gov.br");

            mailSender.send(email);
            logger.info("Email enviado para: " + destinatario);
        } catch (Exception e) {
            logger.severe("Erro ao enviar email: " + e.getMessage());
        }
    }

    /**
     * Cria notificação in-app (para ser implementada com WebSocket).
     */
    private void criarNotificacaoInApp(Long contratoId, String tipo, String titulo, String mensagem) {
        // TODO: Implementar sistema de notificações in-app
        // Pode usar WebSocket para notificações em tempo real
        logger.info(String.format("Notificação in-app criada - Tipo: %s, Contrato: %d", tipo, contratoId));
    }

    /**
     * Envia notificação para múltiplos destinatários.
     */
    public void enviarNotificacaoMultipla(List<String> destinatarios, String assunto, String mensagem) {
        for (String destinatario : destinatarios) {
            enviarEmail(destinatario, assunto, mensagem);
        }
    }

    /**
     * Notifica sobre upload de documento.
     */
    public void notificarUploadDocumento(Contrato contrato, String nomeDocumento, String usuarioUpload) {
        String assunto = "Novo Documento - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "Um novo documento foi adicionado ao contrato %s.\n\n" +
            "Documento: %s\n" +
            "Enviado por: %s\n" +
            "Empresa: %s",
            contrato.getNumeroContrato(),
            nomeDocumento,
            usuarioUpload,
            contrato.getEmpresa().getNome()
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
    }

    /**
     * Notifica sobre pagamento realizado.
     */
    public void notificarPagamentoRealizado(Contrato contrato, String numeroParcela, String valor) {
        String assunto = "Pagamento Realizado - " + contrato.getNumeroContrato();
        String mensagem = String.format(
            "Pagamento realizado para o contrato %s.\n\n" +
            "Parcela: %s\n" +
            "Valor: R$ %s\n" +
            "Empresa: %s",
            contrato.getNumeroContrato(),
            numeroParcela,
            valor,
            contrato.getEmpresa().getNome()
        );

        enviarEmail(contrato.getResponsavel(), assunto, mensagem);
    }
}
