
package com.inventory.stresstester.dto.simulation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MonteCarloRequest(

    @NotNull Long productId,
    @NotNull Long scenarioId,
    @NotNull Long policyId,

    @Min(2) @Max(1000)
    int repetitions,

    String warehouseCode,

    @PositiveOrZero Integer initialInventory,

    @PositiveOrZero BigDecimal orderingCostPerOrder,

    LocalDate startDate,

    Long baseSeed

) {}
