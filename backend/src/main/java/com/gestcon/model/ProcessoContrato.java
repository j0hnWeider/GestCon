package com.gestcon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entidade que representa o processo/workflow de um contrato no sistema GestCon.
 * Permite rastreamento detalhado de todas as etapas e mudanças de status.
 */
@Entity
@Table(name = "processos_contrato")
public class ProcessoContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @NotBlank
    @Column(name = "status_anterior")
    private String statusAnterior;

    @NotBlank
    @Column(name = "status_atual")
    private String statusAtual;

    @NotBlank
    @Column(name = "acao_realizada")
    private String acaoRealizada;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @NotNull
    @Column(name = "data_acao")
    private LocalDateTime dataAcao;

    @NotBlank
    @Column(name = "usuario_responsavel")
    private String usuarioResponsavel;

    @Column(name = "documento_anexo")
    private String documentoAnexo;

    @NotNull
    @Column(name = "ativo")
    private Boolean ativo = true;

    // Construtores
    public ProcessoContrato() {}

    public ProcessoContrato(Contrato contrato, String statusAnterior, String statusAtual, 
                           String acaoRealizada, String usuarioResponsavel) {
        this.contrato = contrato;
        this.statusAnterior = statusAnterior;
        this.statusAtual = statusAtual;
        this.acaoRealizada = acaoRealizada;
        this.usuarioResponsavel = usuarioResponsavel;
        this.dataAcao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public String getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(String statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    public String getAcaoRealizada() {
        return acaoRealizada;
    }

    public void setAcaoRealizada(String acaoRealizada) {
        this.acaoRealizada = acaoRealizada;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataAcao() {
        return dataAcao;
    }

    public void setDataAcao(LocalDateTime dataAcao) {
        this.dataAcao = dataAcao;
    }

    public String getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(String usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public String getDocumentoAnexo() {
        return documentoAnexo;
    }

    public void setDocumentoAnexo(String documentoAnexo) {
        this.documentoAnexo = documentoAnexo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
