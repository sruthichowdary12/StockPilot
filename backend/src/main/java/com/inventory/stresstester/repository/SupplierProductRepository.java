
package com.inventory.stresstester.repository;

import com.inventory.stresstester.entity.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierProductRepository
        extends JpaRepository<SupplierProduct, Long> {

    List<SupplierProduct> findByProduct_Id(Long productId);

    Optional<SupplierProduct>
    findFirstByProduct_IdAndPreferredSupplierTrue(Long productId);
}
