package com.gestcon.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * Classe base abstrata para controllers CRUD.
 * Elimina duplicação de código fornecendo operações básicas para todas as entidades.
 * 
 * @param <T> Tipo da entidade
 * @param <ID> Tipo do identificador da entidade
 */
public abstract class BaseController<T, ID> {

    /**
     * Retorna o repositório específico da entidade.
     * Deve ser implementado pelas classes filhas.
     */
    protected abstract JpaRepository<T, ID> getRepository();

    /**
     * Atualiza os campos da entidade existente com os dados fornecidos.
     * Deve ser implementado pelas classes filhas para definir quais campos atualizar.
     */
    protected abstract void updateEntity(T existingEntity, T entityDetails);

    /**
     * Lista todas as entidades.
     */
    @GetMapping
    public List<T> getAll() {
        return getRepository().findAll();
    }

    /**
     * Busca uma entidade por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        Optional<T> entity = getRepository().findById(id);
        return entity.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova entidade.
     */
    @PostMapping
    public T create(@Valid @RequestBody T entity) {
        return getRepository().save(entity);
    }

    /**
     * Atualiza uma entidade existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @Valid @RequestBody T entityDetails) {
        Optional<T> optionalEntity = getRepository().findById(id);
        if (!optionalEntity.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        T existingEntity = optionalEntity.get();
        updateEntity(existingEntity, entityDetails);
        
        T updatedEntity = getRepository().save(existingEntity);
        return ResponseEntity.ok(updatedEntity);
    }

    /**
     * Remove uma entidade por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        Optional<T> entity = getRepository().findById(id);
        if (!entity.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        getRepository().delete(entity.get());
        return ResponseEntity.noContent().build();
    }
}
