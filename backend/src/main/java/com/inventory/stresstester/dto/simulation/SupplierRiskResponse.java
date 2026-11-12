
package com.inventory.stresstester.dto.simulation;

public record SupplierRiskResponse(

    Long supplierId,
    String supplierName,
    double averageLeadTime,
    double leadTimeStandardDeviation,
    double coefficientOfVariation,
    double supplierRating,
    double riskScore,
    String riskCategory

) {}
