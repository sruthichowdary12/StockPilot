
package com.inventory.stresstester.dto.simulation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SimulationRequest(

    @NotNull Long productId,
    @NotNull Long scenarioId,
    @NotNull Long policyId,

    String warehouseCode,

    @PositiveOrZero Integer initialInventory,

    @PositiveOrZero BigDecimal orderingCostPerOrder,

    LocalDate startDate,

    Long randomSeed,

    @jakarta.validation.constraints.Min(1)
    @jakarta.validation.constraints.Max(3650)
    Integer simulationDays

) {}
