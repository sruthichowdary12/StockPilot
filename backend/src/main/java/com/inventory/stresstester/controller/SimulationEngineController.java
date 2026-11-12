
package com.inventory.stresstester.controller;

import com.inventory.stresstester.dto.simulation.*;
import com.inventory.stresstester.service.*;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation-engine")
public class SimulationEngineController {

    private final InventorySimulationService simulationService;
    private final PolicyComparisonService comparisonService;
    private final MonteCarloSimulationService monteCarloService;

    public SimulationEngineController(
            InventorySimulationService simulationService,
            PolicyComparisonService comparisonService,
            MonteCarloSimulationService monteCarloService) {

        this.simulationService = simulationService;
        this.comparisonService = comparisonService;
        this.monteCarloService = monteCarloService;
    }

    @PostMapping("/run")
    public SimulationResponse runSimulation(
            @Valid @RequestBody SimulationRequest request) {

        return simulationService.run(request);
    }

    @PostMapping("/compare")
    public PolicyComparisonResponse comparePolicies(
            @Valid @RequestBody PolicyComparisonRequest request) {

        return comparisonService.compare(request);
    }

    @PostMapping("/monte-carlo")
    public MonteCarloResponse runMonteCarlo(
            @Valid @RequestBody MonteCarloRequest request) {

        return monteCarloService.run(request);
    }
}
