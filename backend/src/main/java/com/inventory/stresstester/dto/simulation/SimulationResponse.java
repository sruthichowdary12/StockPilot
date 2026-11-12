
package com.inventory.stresstester.dto.simulation;

import java.math.BigDecimal;
import java.util.List;

public record SimulationResponse(

    Long simulationRunId,
    String status,
    int totalDemand,
    int fulfilledDemand,
    int stockoutUnits,
    double serviceLevel,
    double averageInventory,
    BigDecimal totalHoldingCost,
    BigDecimal totalStockoutCost,
    BigDecimal totalOrderingCost,
    BigDecimal totalCost,
    List<DailySimulationResponse> dailyResults

) {}
