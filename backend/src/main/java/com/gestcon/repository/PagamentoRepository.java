package com.gestcon.repository;

import com.gestcon.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository para operações de banco de dados da entidade Pagamento.
 * Fornece métodos para consultas específicas de pagamentos.
 */
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    /**
     * Busca pagamentos por contrato.
     */
    List<Pagamento> findByContratoId(Long contratoId);

    /**
     * Busca pagamentos por status.
     */
    List<Pagamento> findByStatus(String status);

    /**
     * Busca pagamentos atrasados.
     */
    @Query("SELECT p FROM Pagamento p WHERE p.dataVencimento < :dataAtual AND p.status = 'PENDENTE'")
    List<Pagamento> findPagamentosAtrasados(@Param("dataAtual") LocalDate dataAtual);

    /**
     * Busca pagamentos por período de vencimento.
     */
    @Query("SELECT p FROM Pagamento p WHERE p.dataVencimento BETWEEN :dataInicio AND :dataFim")
    List<Pagamento> findByPeriodoVencimento(@Param("dataInicio") LocalDate dataInicio, 
                                           @Param("dataFim") LocalDate dataFim);

    /**
     * Busca pagamentos por usuário responsável.
     */
    List<Pagamento> findByUsuarioResponsavel(String usuarioResponsavel);

    /**
     * Busca pagamentos com vencimento próximo (nos próximos dias).
     */
    @Query("SELECT p FROM Pagamento p WHERE p.dataVencimento BETWEEN :dataAtual AND :dataLimite AND p.status = 'PENDENTE'")
    List<Pagamento> findPagamentosVencimentoProximo(@Param("dataAtual") LocalDate dataAtual, 
                                                    @Param("dataLimite") LocalDate dataLimite);
}
