package com.inventory.stresstester.repository;
import com.inventory.stresstester.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {}
