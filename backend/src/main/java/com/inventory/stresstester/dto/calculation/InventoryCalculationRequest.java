
package com.inventory.stresstester.dto.calculation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record InventoryCalculationRequest(

    @NotNull Long productId,

    @PositiveOrZero Integer leadTimeDays,

    @PositiveOrZero Double serviceLevelZ,

    @PositiveOrZero BigDecimal orderingCostPerOrder

) {}
