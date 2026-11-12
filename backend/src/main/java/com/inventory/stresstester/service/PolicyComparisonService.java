
package com.inventory.stresstester.service;

import com.inventory.stresstester.dto.simulation.*;
import com.inventory.stresstester.entity.Scenario;
import com.inventory.stresstester.repository.ScenarioRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PolicyComparisonService {

    private final InventorySimulationService simulationService;
    private final ScenarioRepository scenarioRepository;

    public PolicyComparisonService(
            InventorySimulationService simulationService,
            ScenarioRepository scenarioRepository) {
        this.simulationService = simulationService;
        this.scenarioRepository = scenarioRepository;
    }

    public PolicyComparisonResponse compare(
            PolicyComparisonRequest request) {

        if (request.firstPolicyId().equals(
                request.secondPolicyId())) {
            throw new IllegalArgumentException(
                    "Choose two different inventory policies");
        }

        Scenario scenario = scenarioRepository
                .findById(request.scenarioId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Scenario not found"));

        long commonSeed = scenario.getRandomSeed() != null
                ? scenario.getRandomSeed()
                : System.currentTimeMillis();

        LocalDate startDate = request.startDate() != null
                ? request.startDate() : LocalDate.now();

        SimulationRequest firstRequest = new SimulationRequest(
                request.productId(),
                request.scenarioId(),
                request.firstPolicyId(),
                request.warehouseCode(),
                request.initialInventory(),
                request.orderingCostPerOrder(),
                startDate, commonSeed, null
        );

        SimulationRequest secondRequest = new SimulationRequest(
                request.productId(),
                request.scenarioId(),
                request.secondPolicyId(),
                request.warehouseCode(),
                request.initialInventory(),
                request.orderingCostPerOrder(),
                startDate,
                commonSeed, null
        );

        SimulationResponse first =
                simulationService.run(firstRequest);

        SimulationResponse second =
                simulationService.run(secondRequest);

        String recommended;
        String reason;

        if (first.totalCost().compareTo(second.totalCost()) < 0) {
            recommended = "FIRST_POLICY";
            reason = "First policy has lower total inventory cost.";
        } else if (second.totalCost().compareTo(first.totalCost()) < 0) {
            recommended = "SECOND_POLICY";
            reason = "Second policy has lower total inventory cost.";
        } else {
            recommended = "EQUAL";
            reason = "Both policies have the same total inventory cost.";
        }

        return new PolicyComparisonResponse(
                first,
                second,
                recommended,
                reason
        );
    }
}
