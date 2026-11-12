package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.InventoryPolicy;
import com.inventory.stresstester.service.InventoryPolicyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryPolicyController {
    private final InventoryPolicyService service;
    public InventoryPolicyController(InventoryPolicyService service) { this.service = service; }

    @GetMapping
    public List<InventoryPolicy> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public InventoryPolicy findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryPolicy create(@Valid @RequestBody InventoryPolicy entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public InventoryPolicy update(@PathVariable Long id, @Valid @RequestBody InventoryPolicy entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
