package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.Supplier;
import com.inventory.stresstester.repository.SupplierRepository;
import org.springframework.stereotype.Service;
@Service
public class SupplierService extends CrudService<Supplier> {
    public SupplierService(SupplierRepository repository) { super(repository); }
}
