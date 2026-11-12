
package com.inventory.stresstester.dto.calculation;

public record InventoryCalculationResponse(

    Long productId,
    double averageDailyDemand,
    double demandStandardDeviation,
    double averageLeadTimeDays,
    double leadTimeStandardDeviation,
    int safetyStock,
    int reorderPoint,
    int inventoryPosition,
    int economicOrderQuantity,
    double serviceLevelZ

) {}
