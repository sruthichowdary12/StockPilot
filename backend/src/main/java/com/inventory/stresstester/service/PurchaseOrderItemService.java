package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.PurchaseOrderItem;
import com.inventory.stresstester.repository.PurchaseOrderItemRepository;
import org.springframework.stereotype.Service;
@Service
public class PurchaseOrderItemService extends CrudService<PurchaseOrderItem> {
    public PurchaseOrderItemService(PurchaseOrderItemRepository repository) { super(repository); }
}
