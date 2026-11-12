package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.SimulationRun;
import com.inventory.stresstester.service.SimulationRunService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/simulations")
@CrossOrigin(origins = "http://localhost:3000")
public class SimulationRunController {
    private final SimulationRunService service;
    public SimulationRunController(SimulationRunService service) { this.service = service; }

    @GetMapping
    public List<SimulationRun> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public SimulationRun findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimulationRun create(@Valid @RequestBody SimulationRun entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public SimulationRun update(@PathVariable Long id, @Valid @RequestBody SimulationRun entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
