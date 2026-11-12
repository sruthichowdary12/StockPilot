package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.InventoryTransaction;
import com.inventory.stresstester.repository.InventoryTransactionRepository;
import org.springframework.stereotype.Service;
@Service
public class InventoryTransactionService extends CrudService<InventoryTransaction> {
    public InventoryTransactionService(InventoryTransactionRepository repository) { super(repository); }
}
