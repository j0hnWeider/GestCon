package com.gestcon.repository;

import com.gestcon.model.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository para operações de banco de dados da entidade NotaFiscal.
 * Fornece métodos para consultas específicas de notas fiscais.
 */
@Repository
public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {

    /**
     * Busca notas fiscais por contrato.
     */
    List<NotaFiscal> findByContratoId(Long contratoId);

    /**
     * Busca notas fiscais por status.
     */
    List<NotaFiscal> findByStatus(String status);

    /**
     * Busca notas fiscais vencidas.
     */
    @Query("SELECT nf FROM NotaFiscal nf WHERE nf.dataVencimento < :dataAtual AND nf.status != 'PAGA'")
    List<NotaFiscal> findNotasVencidas(@Param("dataAtual") LocalDate dataAtual);

    /**
     * Busca notas fiscais por número.
     */
    NotaFiscal findByNumeroNota(String numeroNota);

    /**
     * Busca notas fiscais por período de emissão.
     */
    @Query("SELECT nf FROM NotaFiscal nf WHERE nf.dataEmissao BETWEEN :dataInicio AND :dataFim")
    List<NotaFiscal> findByPeriodoEmissao(@Param("dataInicio") LocalDate dataInicio, 
                                         @Param("dataFim") LocalDate dataFim);
}
