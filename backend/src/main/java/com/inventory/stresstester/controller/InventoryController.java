package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.Inventory;
import com.inventory.stresstester.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryController {
    private final InventoryService service;
    public InventoryController(InventoryService service) { this.service = service; }

    @GetMapping
    public List<Inventory> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Inventory findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inventory create(@Valid @RequestBody Inventory entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public Inventory update(@PathVariable Long id, @Valid @RequestBody Inventory entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
