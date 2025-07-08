package com.gestcon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa um pagamento relacionado a um contrato no sistema GestCon.
 * Permite controle financeiro detalhado de cada parcela ou pagamento.
 */
@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @NotBlank
    @Column(name = "numero_parcela")
    private String numeroParcela;

    @NotNull
    @Column(name = "valor_previsto", precision = 15, scale = 2)
    private BigDecimal valorPrevisto;

    @Column(name = "valor_pago", precision = 15, scale = 2)
    private BigDecimal valorPago;

    @NotNull
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @NotBlank
    @Column(name = "status")
    private String status; // PENDENTE, PAGO, ATRASADO, CANCELADO

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "numero_nota_fiscal")
    private String numeroNotaFiscal;

    @Column(name = "documento_comprobatorio")
    private String documentoComprobatorio;

    @NotNull
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @NotBlank
    @Column(name = "usuario_responsavel")
    private String usuarioResponsavel;

    // Construtores
    public Pagamento() {
        this.dataCriacao = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    public Pagamento(Contrato contrato, String numeroParcela, BigDecimal valorPrevisto, 
                    LocalDate dataVencimento, String usuarioResponsavel) {
        this();
        this.contrato = contrato;
        this.numeroParcela = numeroParcela;
        this.valorPrevisto = valorPrevisto;
        this.dataVencimento = dataVencimento;
        this.usuarioResponsavel = usuarioResponsavel;
    }

    // Métodos de negócio
    public boolean isAtrasado() {
        return "PENDENTE".equals(status) && dataVencimento.isBefore(LocalDate.now());
    }

    public BigDecimal calcularDiferenca() {
        if (valorPago != null) {
            return valorPago.subtract(valorPrevisto);
        }
        return BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
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

    public String getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(String numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public BigDecimal getValorPrevisto() {
        return valorPrevisto;
    }

    public void setValorPrevisto(BigDecimal valorPrevisto) {
        this.valorPrevisto = valorPrevisto;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(String numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    public String getDocumentoComprobatorio() {
        return documentoComprobatorio;
    }

    public void setDocumentoComprobatorio(String documentoComprobatorio) {
        this.documentoComprobatorio = documentoComprobatorio;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(String usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }
}
