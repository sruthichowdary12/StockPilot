package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.SupplierProduct;
import com.inventory.stresstester.service.SupplierProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-products")
@CrossOrigin(origins = "http://localhost:3000")
public class SupplierProductController {
    private final SupplierProductService service;
    public SupplierProductController(SupplierProductService service) { this.service = service; }

    @GetMapping
    public List<SupplierProduct> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public SupplierProduct findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierProduct create(@Valid @RequestBody SupplierProduct entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public SupplierProduct update(@PathVariable Long id, @Valid @RequestBody SupplierProduct entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
