package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.SimulationResult;
import com.inventory.stresstester.service.SimulationResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/simulation-results")
@CrossOrigin(origins = "http://localhost:3000")
public class SimulationResultController {
    private final SimulationResultService service;
    public SimulationResultController(SimulationResultService service) { this.service = service; }

    @GetMapping
    public List<SimulationResult> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public SimulationResult findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimulationResult create(@Valid @RequestBody SimulationResult entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public SimulationResult update(@PathVariable Long id, @Valid @RequestBody SimulationResult entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
