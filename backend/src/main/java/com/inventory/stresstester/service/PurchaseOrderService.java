package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.PurchaseOrder;
import com.inventory.stresstester.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
@Service
public class PurchaseOrderService extends CrudService<PurchaseOrder> {
    public PurchaseOrderService(PurchaseOrderRepository repository) { super(repository); }
}
