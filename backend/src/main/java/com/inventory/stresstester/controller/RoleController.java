package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.Role;
import com.inventory.stresstester.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:3000")
public class RoleController {
    private final RoleService service;
    public RoleController(RoleService service) { this.service = service; }

    @GetMapping
    public List<Role> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Role findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Role create(@Valid @RequestBody Role entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public Role update(@PathVariable Long id, @Valid @RequestBody Role entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
