
package com.inventory.stresstester.service;

import com.inventory.stresstester.dto.calculation.InventoryCalculationRequest;
import com.inventory.stresstester.dto.calculation.InventoryCalculationResponse;
import com.inventory.stresstester.entity.*;
import com.inventory.stresstester.repository.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryCalculationService {

    private final ProductRepository productRepository;
    private final DemandRecordRepository demandRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final InventoryRepository inventoryRepository;

    public InventoryCalculationService(
            ProductRepository productRepository,
            DemandRecordRepository demandRepository,
            SupplierProductRepository supplierProductRepository,
            InventoryRepository inventoryRepository) {

        this.productRepository = productRepository;
        this.demandRepository = demandRepository;
        this.supplierProductRepository = supplierProductRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryCalculationResponse calculate(
            InventoryCalculationRequest request) {

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Product not found"));

        List<DemandRecord> records =
                demandRepository.findByProduct_IdOrderByDemandDateAsc(
                        product.getId());

        if (records.isEmpty()) {
            throw new IllegalArgumentException(
                    "Historical demand records are required");
        }


double[] adjustedDemand = records.stream()
        .mapToDouble(record -> {

            double factor = record.getSeasonalityFactor() == null
                    ? 1.0
                    : record.getSeasonalityFactor();

            if (factor < 0) {
                throw new IllegalArgumentException(
                        "Seasonality factor cannot be negative");
            }

            return record.getDemandQuantity() * factor;
        })
        .toArray();

double averageDemand = java.util.Arrays.stream(adjustedDemand)
        .average()
        .orElse(0);

double variance = 0;

if (adjustedDemand.length > 1) {

    for (double quantity : adjustedDemand) {
        double difference = quantity - averageDemand;
        variance += difference * difference;
    }

    variance /= adjustedDemand.length - 1;
}

double demandStdDev = Math.sqrt(variance);


        

        SupplierProduct supplierProduct =
                supplierProductRepository
                    .findFirstByProduct_IdAndPreferredSupplierTrue(
                            product.getId())
                    .orElse(null);

        int leadTime = request.leadTimeDays() != null
                ? request.leadTimeDays()
                : supplierProduct != null
                    && supplierProduct.getLeadTimeDays() != null
                        ? supplierProduct.getLeadTimeDays()
                        : supplierProduct != null
                            && supplierProduct.getSupplier()
                                .getDefaultLeadTimeDays() != null
                                ? supplierProduct.getSupplier()
                                    .getDefaultLeadTimeDays()
                                : 1;

        double leadTimeStdDev = supplierProduct != null
                && supplierProduct.getSupplier()
                    .getLeadTimeStdDevDays() != null
                        ? supplierProduct.getSupplier()
                            .getLeadTimeStdDevDays()
                        : 0;

        double z = request.serviceLevelZ() != null
                ? request.serviceLevelZ()
                : 1.65;

        if (leadTime < 1 || z < 0 || leadTimeStdDev < 0) {
            throw new IllegalArgumentException(
                    "Lead time, Z factor or supplier variability is invalid");
        }

        double safetyStockValue = z * Math.sqrt(
                leadTime * demandStdDev * demandStdDev
                + averageDemand * averageDemand
                    * leadTimeStdDev * leadTimeStdDev
        );

        int safetyStock = (int) Math.ceil(safetyStockValue);

        int reorderPoint = (int) Math.ceil(
                averageDemand * leadTime + safetyStock
        );

        Inventory inventory = inventoryRepository
                .findByProduct_IdAndWarehouseCode(
                        product.getId(), "DEFAULT")
                .orElse(null);

        int inventoryPosition = inventory == null
                ? 0
                : inventory.getQuantityOnHand()
                    + inventory.getQuantityOnOrder()
                    - inventory.getBackorderQuantity();

        double annualDemand = averageDemand * 365;

        double orderingCost = request.orderingCostPerOrder() != null
                ? request.orderingCostPerOrder().doubleValue()
                : 100.0;
        double holdingCost = product.getHoldingCostPerUnitDay() != null
                ? product.getHoldingCostPerUnitDay().doubleValue() * 365
                : 0;

        int eoq = holdingCost > 0
                ? (int) Math.ceil(Math.sqrt(
                    (2 * annualDemand * orderingCost) / holdingCost))
                : 0;

        return new InventoryCalculationResponse(
                product.getId(),
                averageDemand,
                demandStdDev,
                leadTime,
                leadTimeStdDev,
                safetyStock,
                reorderPoint,
                inventoryPosition,
                eoq,
                z
        );
    }
}
