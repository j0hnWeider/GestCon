package com.gestcon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma Nota Fiscal vinculada a um contrato no sistema GestCon.
 * Permite controle detalhado de documentos fiscais e sua validação.
 */
@Entity
@Table(name = "notas_fiscais")
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @ManyToOne
    @JoinColumn(name = "pagamento_id")
    private Pagamento pagamento;

    @NotBlank
    @Column(name = "numero_nota", unique = true)
    private String numeroNota;

    @NotBlank
    @Column(name = "serie")
    private String serie;

    @NotNull
    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_impostos", precision = 15, scale = 2)
    private BigDecimal valorImpostos;

    @Column(name = "valor_liquido", precision = 15, scale = 2)
    private BigDecimal valorLiquido;

    @NotNull
    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @NotBlank
    @Column(name = "status")
    private String status; // PENDENTE, APROVADA, REJEITADA, PAGA

    @Column(name = "descricao_servicos", columnDefinition = "TEXT")
    private String descricaoServicos;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "arquivo_pdf")
    private String arquivoPdf;

    @Column(name = "arquivo_xml")
    private String arquivoXml;

    @Column(name = "chave_acesso")
    private String chaveAcesso;

    @NotNull
    @Column(name = "data_upload")
    private LocalDateTime dataUpload;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "usuario_upload")
    private String usuarioUpload;

    @Column(name = "usuario_aprovacao")
    private String usuarioAprovacao;

    @Column(name = "motivo_rejeicao", columnDefinition = "TEXT")
    private String motivoRejeicao;

    // Construtores
    public NotaFiscal() {
        this.dataUpload = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    public NotaFiscal(Contrato contrato, String numeroNota, String serie, 
                     BigDecimal valorTotal, LocalDate dataEmissao, String usuarioUpload) {
        this();
        this.contrato = contrato;
        this.numeroNota = numeroNota;
        this.serie = serie;
        this.valorTotal = valorTotal;
        this.dataEmissao = dataEmissao;
        this.usuarioUpload = usuarioUpload;
        this.calcularValorLiquido();
    }

    // Métodos de negócio
    public void calcularValorLiquido() {
        if (valorTotal != null) {
            BigDecimal impostos = valorImpostos != null ? valorImpostos : BigDecimal.ZERO;
            this.valorLiquido = valorTotal.subtract(impostos);
        }
    }

    public void aprovar(String usuarioAprovacao) {
        this.status = "APROVADA";
        this.usuarioAprovacao = usuarioAprovacao;
        this.dataAprovacao = LocalDateTime.now();
        this.motivoRejeicao = null;
    }

    public void rejeitar(String usuarioAprovacao, String motivo) {
        this.status = "REJEITADA";
        this.usuarioAprovacao = usuarioAprovacao;
        this.dataAprovacao = LocalDateTime.now();
        this.motivoRejeicao = motivo;
    }

    public boolean isVencida() {
        return dataVencimento != null && dataVencimento.isBefore(LocalDate.now()) 
               && !"PAGA".equals(status);
    }

    @PreUpdate
    public void preUpdate() {
        calcularValorLiquido();
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

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
        calcularValorLiquido();
    }

    public BigDecimal getValorImpostos() {
        return valorImpostos;
    }

    public void setValorImpostos(BigDecimal valorImpostos) {
        this.valorImpostos = valorImpostos;
        calcularValorLiquido();
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescricaoServicos() {
        return descricaoServicos;
    }

    public void setDescricaoServicos(String descricaoServicos) {
        this.descricaoServicos = descricaoServicos;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getArquivoPdf() {
        return arquivoPdf;
    }

    public void setArquivoPdf(String arquivoPdf) {
        this.arquivoPdf = arquivoPdf;
    }

    public String getArquivoXml() {
        return arquivoXml;
    }

    public void setArquivoXml(String arquivoXml) {
        this.arquivoXml = arquivoXml;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public String getUsuarioUpload() {
        return usuarioUpload;
    }

    public void setUsuarioUpload(String usuarioUpload) {
        this.usuarioUpload = usuarioUpload;
    }

    public String getUsuarioAprovacao() {
        return usuarioAprovacao;
    }

    public void setUsuarioAprovacao(String usuarioAprovacao) {
        this.usuarioAprovacao = usuarioAprovacao;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }
}
