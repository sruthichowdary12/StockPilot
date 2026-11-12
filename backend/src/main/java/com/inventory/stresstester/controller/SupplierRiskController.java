
package com.inventory.stresstester.controller;

import com.inventory.stresstester.dto.simulation.SupplierRiskResponse;
import com.inventory.stresstester.service.SupplierRiskAnalysisService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis/suppliers")
public class SupplierRiskController {

    private final SupplierRiskAnalysisService riskService;

    public SupplierRiskController(
            SupplierRiskAnalysisService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/{supplierId}/risk")
    public SupplierRiskResponse analyseSupplier(
            @PathVariable Long supplierId) {

        return riskService.analyse(supplierId);
    }
}
