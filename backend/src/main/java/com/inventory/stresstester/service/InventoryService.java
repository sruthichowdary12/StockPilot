package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.Inventory;
import com.inventory.stresstester.repository.InventoryRepository;
import org.springframework.stereotype.Service;
@Service
public class InventoryService extends CrudService<Inventory> {
    public InventoryService(InventoryRepository repository) { super(repository); }
}
