package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.PurchaseOrder;
import com.inventory.stresstester.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "http://localhost:3000")
public class PurchaseOrderController {
    private final PurchaseOrderService service;
    public PurchaseOrderController(PurchaseOrderService service) { this.service = service; }

    @GetMapping
    public List<PurchaseOrder> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public PurchaseOrder findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseOrder create(@Valid @RequestBody PurchaseOrder entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public PurchaseOrder update(@PathVariable Long id, @Valid @RequestBody PurchaseOrder entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
