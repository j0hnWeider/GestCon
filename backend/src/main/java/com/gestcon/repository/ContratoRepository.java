package com.gestcon.repository;

import com.gestcon.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    // Métodos customizados podem ser adicionados aqui
}
