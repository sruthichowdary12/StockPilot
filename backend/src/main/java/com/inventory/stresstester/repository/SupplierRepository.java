package com.inventory.stresstester.repository;
import com.inventory.stresstester.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SupplierRepository extends JpaRepository<Supplier, Long> {}
