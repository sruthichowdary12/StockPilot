package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.Scenario;
import com.inventory.stresstester.service.ScenarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/scenarios")
@CrossOrigin(origins = "http://localhost:3000")
public class ScenarioController {
    private final ScenarioService service;
    public ScenarioController(ScenarioService service) { this.service = service; }

    @GetMapping
    public List<Scenario> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Scenario findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Scenario create(@Valid @RequestBody Scenario entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public Scenario update(@PathVariable Long id, @Valid @RequestBody Scenario entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
