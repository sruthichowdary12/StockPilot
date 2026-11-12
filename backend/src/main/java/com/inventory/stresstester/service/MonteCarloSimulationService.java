
package com.inventory.stresstester.service;

import com.inventory.stresstester.dto.simulation.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MonteCarloSimulationService {

    private final InventorySimulationService simulationService;

    public MonteCarloSimulationService(
            InventorySimulationService simulationService) {
        this.simulationService = simulationService;
    }

    public MonteCarloResponse run(MonteCarloRequest request) {

        if (request.repetitions() < 2
                || request.repetitions() > 1000) {
            throw new IllegalArgumentException(
                    "Repetitions must be between 2 and 1000");
        }

        long baseSeed = request.baseSeed() != null
                ? request.baseSeed()
                : System.currentTimeMillis();

        List<SimulationResponse> runs = new ArrayList<>();

        double serviceSum = 0;
        double costSum = 0;
        double stockoutSum = 0;
        double inventorySum = 0;
        double worstStockout = 0;

        for (int i = 0; i < request.repetitions(); i++) {

            SimulationRequest simulationRequest =
                    new SimulationRequest(
                            request.productId(),
                            request.scenarioId(),
                            request.policyId(),
                            request.warehouseCode(),
                            request.initialInventory(),
                            request.orderingCostPerOrder(),
                            request.startDate(),
                            baseSeed + i, null
                    );

            SimulationResponse result =
                    simulationService.run(simulationRequest);

            runs.add(result);

            serviceSum += result.serviceLevel();
            costSum += result.totalCost().doubleValue();
            stockoutSum += result.stockoutUnits();
            inventorySum += result.averageInventory();

            worstStockout = Math.max(
                    worstStockout, result.stockoutUnits());
        }

        double averageService = serviceSum / request.repetitions();
        double averageCost = costSum / request.repetitions();
        double averageStockout = stockoutSum / request.repetitions();
        double averageInventory = inventorySum / request.repetitions();

        double variance = 0;

        for (SimulationResponse result : runs) {
            double difference =
                    result.serviceLevel() - averageService;

            variance += difference * difference;
        }

        double serviceStdDev = Math.sqrt(
                variance / (request.repetitions() - 1));

        return new MonteCarloResponse(
                request.repetitions(),
                averageService,
                averageCost,
                averageStockout,
                worstStockout,
                averageInventory,
                serviceStdDev,
                runs
        );
    }
}
