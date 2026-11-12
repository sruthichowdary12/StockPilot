package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.PurchaseOrderItem;
import com.inventory.stresstester.service.PurchaseOrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-order-items")
@CrossOrigin(origins = "http://localhost:3000")
public class PurchaseOrderItemController {
    private final PurchaseOrderItemService service;
    public PurchaseOrderItemController(PurchaseOrderItemService service) { this.service = service; }

    @GetMapping
    public List<PurchaseOrderItem> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public PurchaseOrderItem findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseOrderItem create(@Valid @RequestBody PurchaseOrderItem entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public PurchaseOrderItem update(@PathVariable Long id, @Valid @RequestBody PurchaseOrderItem entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
