package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.AppUser;
import com.inventory.stresstester.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class AppUserController {
    private final AppUserService service;
    public AppUserController(AppUserService service) { this.service = service; }

    @GetMapping
    public List<AppUser> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public AppUser findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser create(@Valid @RequestBody AppUser entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public AppUser update(@PathVariable Long id, @Valid @RequestBody AppUser entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
