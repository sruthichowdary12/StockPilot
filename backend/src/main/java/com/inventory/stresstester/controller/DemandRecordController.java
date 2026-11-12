package com.inventory.stresstester.controller;

import com.inventory.stresstester.entity.DemandRecord;
import com.inventory.stresstester.service.DemandRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/demand-records")
@CrossOrigin(origins = "http://localhost:3000")
public class DemandRecordController {
    private final DemandRecordService service;
    public DemandRecordController(DemandRecordService service) { this.service = service; }

    @GetMapping
    public List<DemandRecord> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public DemandRecord findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemandRecord create(@Valid @RequestBody DemandRecord entity) { return service.create(entity); }

    @PutMapping("/{id}")
    public DemandRecord update(@PathVariable Long id, @Valid @RequestBody DemandRecord entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
