package com.inventory.stresstester.repository;
import com.inventory.stresstester.entity.InventoryPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryPolicyRepository extends JpaRepository<InventoryPolicy, Long> {}
