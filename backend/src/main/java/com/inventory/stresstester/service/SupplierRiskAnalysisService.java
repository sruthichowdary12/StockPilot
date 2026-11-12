
package com.inventory.stresstester.service;

import com.inventory.stresstester.dto.simulation.SupplierRiskResponse;
import com.inventory.stresstester.entity.Supplier;
import com.inventory.stresstester.repository.SupplierRepository;

import org.springframework.stereotype.Service;

@Service
public class SupplierRiskAnalysisService {

    private final SupplierRepository supplierRepository;

    public SupplierRiskAnalysisService(
            SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierRiskResponse analyse(Long supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Supplier not found"));

        double averageLeadTime =
                supplier.getDefaultLeadTimeDays() == null
                        ? 0 : supplier.getDefaultLeadTimeDays();

        double deviation =
                supplier.getLeadTimeStdDevDays() == null
                        ? 0 : supplier.getLeadTimeStdDevDays();

        double rating =
                supplier.getSupplierRating() == null
                        ? 2.5 : supplier.getSupplierRating();

        if (averageLeadTime < 0 || deviation < 0
                || rating < 0 || rating > 5) {
            throw new IllegalArgumentException(
                    "Invalid supplier risk inputs");
        }

        double coefficientOfVariation =
                averageLeadTime == 0
                        ? 0 : deviation / averageLeadTime;

        double variabilityRisk =
                coefficientOfVariation
                        / (1 + coefficientOfVariation);

        double ratingRisk = (5 - rating) / 5;

        double riskScore =
                (variabilityRisk * 60 + ratingRisk * 40);

        String category;

        if (riskScore < 25) {
            category = "LOW";
        } else if (riskScore < 50) {
            category = "MEDIUM";
        } else if (riskScore < 75) {
            category = "HIGH";
        } else {
            category = "CRITICAL";
        }

        return new SupplierRiskResponse(
                supplier.getId(),
                supplier.getSupplierName(),
                averageLeadTime,
                deviation,
                coefficientOfVariation,
                rating,
                Math.round(riskScore * 100.0) / 100.0,
                category
        );
    }
}
