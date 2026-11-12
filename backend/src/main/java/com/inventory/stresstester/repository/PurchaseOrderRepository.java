package com.inventory.stresstester.repository;
import com.inventory.stresstester.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {}
