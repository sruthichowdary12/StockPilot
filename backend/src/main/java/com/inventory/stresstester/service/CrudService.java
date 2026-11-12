package com.inventory.stresstester.service;

import com.inventory.stresstester.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public abstract class CrudService<T> {
    protected final JpaRepository<T, Long> repository;

    protected CrudService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }

    public T create(T entity) {
        return repository.save(entity);
    }

    public T update(Long id, T entity) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }
        if (!(entity instanceof BaseEntity baseEntity)) {
            throw new IllegalArgumentException("All managed entities must extend BaseEntity");
        }
        baseEntity.setId(id);
        return repository.save(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
