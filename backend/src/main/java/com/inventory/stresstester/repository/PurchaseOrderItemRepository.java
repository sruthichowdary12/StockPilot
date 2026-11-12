package com.inventory.stresstester.repository;
import com.inventory.stresstester.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {}
