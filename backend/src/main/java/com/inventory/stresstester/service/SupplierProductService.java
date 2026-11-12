package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.SupplierProduct;
import com.inventory.stresstester.repository.SupplierProductRepository;
import org.springframework.stereotype.Service;
@Service
public class SupplierProductService extends CrudService<SupplierProduct> {
    public SupplierProductService(SupplierProductRepository repository) { super(repository); }
}
