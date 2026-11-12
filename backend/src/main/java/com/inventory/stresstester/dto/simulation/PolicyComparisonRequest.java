
package com.inventory.stresstester.dto.simulation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyComparisonRequest(

    @NotNull
    Long productId,

    @NotNull
    Long scenarioId,

    @NotNull
    Long firstPolicyId,

    @NotNull
    Long secondPolicyId,

    String warehouseCode,

    @PositiveOrZero
    Integer initialInventory,

    @PositiveOrZero
    BigDecimal orderingCostPerOrder,

    LocalDate startDate

) {}
