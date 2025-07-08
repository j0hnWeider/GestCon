package com.gestcon.repository;

import com.gestcon.model.ProcessoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório JPA para a entidade ProcessoContrato.
 * Fornece métodos para consultas relacionadas ao workflow de contratos.
 */
@Repository
public interface ProcessoContratoRepository extends JpaRepository<ProcessoContrato, Long> {

    /**
     * Busca todos os processos de um contrato ordenados por data decrescente.
     */
    List<ProcessoContrato> findByContratoIdOrderByDataAcaoDesc(Long contratoId);

    /**
     * Busca processos por status atual.
     */
    List<ProcessoContrato> findByStatusAtual(String statusAtual);

    /**
     * Busca processos por usuário responsável.
     */
    List<ProcessoContrato> findByUsuarioResponsavel(String usuarioResponsavel);

    /**
     * Busca processos em um período específico.
     */
    List<ProcessoContrato> findByDataAcaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * Busca o último processo de um contrato.
     */
    @Query("SELECT p FROM ProcessoContrato p WHERE p.contrato.id = :contratoId ORDER BY p.dataAcao DESC LIMIT 1")
    ProcessoContrato findUltimoProcessoContrato(@Param("contratoId") Long contratoId);

    /**
     * Conta processos por status atual.
     */
    @Query("SELECT p.statusAtual, COUNT(p) FROM ProcessoContrato p GROUP BY p.statusAtual")
    List<Object[]> countByStatusAtual();

    /**
     * Busca processos ativos (não arquivados).
     */
    List<ProcessoContrato> findByAtivoTrue();

    /**
     * Busca processos por ação realizada.
     */
    List<ProcessoContrato> findByAcaoRealizada(String acaoRealizada);

    /**
     * Busca processos que possuem documentos anexos.
     */
    @Query("SELECT p FROM ProcessoContrato p WHERE p.documentoAnexo IS NOT NULL")
    List<ProcessoContrato> findProcessosComDocumentos();

    /**
     * Busca estatísticas de processos por período.
     */
    @Query("SELECT DATE(p.dataAcao), COUNT(p) FROM ProcessoContrato p WHERE p.dataAcao BETWEEN :dataInicio AND :dataFim GROUP BY DATE(p.dataAcao)")
    List<Object[]> findEstatisticasPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio, 
                                             @Param("dataFim") LocalDateTime dataFim);
}
