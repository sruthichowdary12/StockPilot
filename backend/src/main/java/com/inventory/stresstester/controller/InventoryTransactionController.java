package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.InventoryTransaction;
import com.inventory.stresstester.service.InventoryTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory-transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryTransactionController {
    private final InventoryTransactionService service;
    public InventoryTransactionController(InventoryTransactionService service) { this.service = service; }

    @GetMapping
    public List<InventoryTransaction> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public InventoryTransaction findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryTransaction create(@Valid @RequestBody InventoryTransaction entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public InventoryTransaction update(@PathVariable Long id, @Valid @RequestBody InventoryTransaction entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
