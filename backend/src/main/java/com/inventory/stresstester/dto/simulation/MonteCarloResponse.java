
package com.inventory.stresstester.dto.simulation;

import java.util.List;

public record MonteCarloResponse(

    int repetitions,
    double averageServiceLevel,
    double averageTotalCost,
    double averageStockoutUnits,
    double worstStockoutUnits,
    double averageInventory,
    double serviceLevelStandardDeviation,
    List<SimulationResponse> runs

) {}
