
package com.inventory.stresstester.controller;

import com.inventory.stresstester.dto.calculation.InventoryCalculationRequest;
import com.inventory.stresstester.dto.calculation.InventoryCalculationResponse;
import com.inventory.stresstester.service.InventoryCalculationService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculations")
public class InventoryCalculationController {

    private final InventoryCalculationService calculationService;

    public InventoryCalculationController(
            InventoryCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/inventory")
    public InventoryCalculationResponse calculateInventory(
            @Valid @RequestBody InventoryCalculationRequest request) {
        return calculationService.calculate(request);
    }
}
