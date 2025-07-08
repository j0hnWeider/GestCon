package com.gestcon.repository;

import com.gestcon.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade Empresa.
 */
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    // Métodos customizados podem ser adicionados aqui
}
