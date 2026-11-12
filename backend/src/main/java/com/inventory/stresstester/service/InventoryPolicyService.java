package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.InventoryPolicy;
import com.inventory.stresstester.repository.InventoryPolicyRepository;
import org.springframework.stereotype.Service;
@Service
public class InventoryPolicyService extends CrudService<InventoryPolicy> {
    public InventoryPolicyService(InventoryPolicyRepository repository) { super(repository); }
}
