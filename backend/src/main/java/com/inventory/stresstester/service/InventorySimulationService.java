
package com.inventory.stresstester.service;

import com.inventory.stresstester.dto.calculation.InventoryCalculationRequest;
import com.inventory.stresstester.dto.calculation.InventoryCalculationResponse;
import com.inventory.stresstester.dto.simulation.*;
import com.inventory.stresstester.entity.*;
import com.inventory.stresstester.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class InventorySimulationService {

    private final ProductRepository productRepository;
    private final InventoryPolicyRepository policyRepository;
    private final ScenarioRepository scenarioRepository;
    private final InventoryRepository inventoryRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final SimulationRunRepository runRepository;
    private final SimulationResultRepository resultRepository;
    private final InventoryCalculationService calculationService;
    private final DemandScenarioGenerator demandGenerator;
    private final LeadTimeScenarioGenerator leadTimeGenerator;
    private final DailyInventoryProcessor dailyProcessor;

    public InventorySimulationService(
            ProductRepository productRepository,
            InventoryPolicyRepository policyRepository,
            ScenarioRepository scenarioRepository,
            InventoryRepository inventoryRepository,
            SupplierProductRepository supplierProductRepository,
            SimulationRunRepository runRepository,
            SimulationResultRepository resultRepository,
            InventoryCalculationService calculationService,
            DemandScenarioGenerator demandGenerator,
            LeadTimeScenarioGenerator leadTimeGenerator,
            DailyInventoryProcessor dailyProcessor) {

        this.productRepository = productRepository;
        this.policyRepository = policyRepository;
        this.scenarioRepository = scenarioRepository;
        this.inventoryRepository = inventoryRepository;
        this.supplierProductRepository = supplierProductRepository;
        this.runRepository = runRepository;
        this.resultRepository = resultRepository;
        this.calculationService = calculationService;
        this.demandGenerator = demandGenerator;
        this.leadTimeGenerator = leadTimeGenerator;
        this.dailyProcessor = dailyProcessor;
    }

    @Transactional
    public SimulationResponse run(SimulationRequest request) {

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Product not found"));

        InventoryPolicy policy = policyRepository.findById(request.policyId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Policy not found"));

        Scenario scenario = scenarioRepository.findById(request.scenarioId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Scenario not found"));

        if (policy.getProduct() == null ||
                !policy.getProduct().getId().equals(product.getId())) {
            throw new IllegalArgumentException(
                    "The selected policy does not belong to this product");
        }

        int days = request.simulationDays() != null
                ? request.simulationDays() : scenario.getSimulationDays();

        if (days < 1 || days > 3650) {
            throw new IllegalArgumentException(
                    "Simulation days must be between 1 and 3650");
        }

        double demandMultiplier =
                scenario.getDemandMultiplier() == null
                        ? 1.0 : scenario.getDemandMultiplier();

        double leadMultiplier =
                scenario.getLeadTimeMultiplier() == null
                        ? 1.0 : scenario.getLeadTimeMultiplier();

        if (demandMultiplier < 0 || leadMultiplier <= 0) {
            throw new IllegalArgumentException(
                    "Invalid scenario multipliers");
        }

        InventoryCalculationResponse calculation =
                calculationService.calculate(
                		new InventoryCalculationRequest(
                		        product.getId(), null, 1.65, request.orderingCostPerOrder()));

        SupplierProduct supplierProduct =
                supplierProductRepository
                    .findFirstByProduct_IdAndPreferredSupplierTrue(
                            product.getId())
                    .orElse(null);

        int minimumOrderQuantity = supplierProduct != null
                && supplierProduct.getMinimumOrderQuantity() != null
                        ? supplierProduct.getMinimumOrderQuantity()
                        : 1;

        long seed = request.randomSeed() != null
                ? request.randomSeed()
                : scenario.getRandomSeed() != null
                    ? scenario.getRandomSeed()
                    : System.currentTimeMillis();

        Random random = new Random(seed);

        String warehouse = request.warehouseCode() == null
                || request.warehouseCode().isBlank()
                        ? "DEFAULT" : request.warehouseCode();

        Inventory inventory = inventoryRepository
                .findByProduct_IdAndWarehouseCode(
                        product.getId(), warehouse)
                .orElse(null);

        int onHand = request.initialInventory() != null
                ? request.initialInventory()
                : inventory != null
                    ? inventory.getQuantityOnHand()
                    : 0;

        if (onHand < 0) {
            throw new IllegalArgumentException(
                    "Initial inventory cannot be negative");
        }

        int backorders = inventory != null
                ? inventory.getBackorderQuantity() : 0;

        if (backorders < 0) {
            throw new IllegalArgumentException(
                    "Backorders cannot be negative");
        }

        if (policy.getPolicyType() == null) {
            throw new IllegalArgumentException("Inventory policy type is required");
        }

        int reorderPoint = policy.getReorderPoint() != null
                ? policy.getReorderPoint()
                : calculation.reorderPoint();

        int orderQuantity = policy.getOrderQuantity() != null
                ? policy.getOrderQuantity()
                : Math.max(1, calculation.economicOrderQuantity());

        int targetStock = policy.getTargetStockLevel() != null
                ? policy.getTargetStockLevel()
                : reorderPoint + orderQuantity;

        int reviewInterval = policy.getReviewIntervalDays() != null
                ? policy.getReviewIntervalDays() : 1;

        if (reorderPoint < 0 || orderQuantity < 1
                || targetStock < 0 || reviewInterval < 1) {
            throw new IllegalArgumentException(
                    "Invalid inventory policy configuration");
        }

        BigDecimal orderingCost = request.orderingCostPerOrder() != null
                ? request.orderingCostPerOrder()
                : new BigDecimal("100.00");

        BigDecimal holdingCostPerUnit =
                product.getHoldingCostPerUnitDay() != null
                        ? product.getHoldingCostPerUnitDay()
                        : BigDecimal.ZERO;

        BigDecimal stockoutCostPerUnit =
                product.getStockoutCostPerUnit() != null
                        ? product.getStockoutCostPerUnit()
                        : BigDecimal.ZERO;

        LocalDate startDate = request.startDate() != null
                ? request.startDate() : LocalDate.now();

        SimulationRun run = new SimulationRun();
        run.setScenario(scenario);
        run.setPolicy(policy);
        run.setProduct(product);
        run.setStatus(SimulationRun.RunStatus.RUNNING);
        run.setStartDate(startDate);
        run.setEndDate(startDate.plusDays(days - 1));
        run.setRandomSeed(seed);

        run = runRepository.save(run);

        // Key = delivery day, Value = quantity arriving that day.
        Map<Integer, Integer> incomingSchedule = new HashMap<>();

        List<SimulationResult> savedResults = new ArrayList<>();
        List<DailySimulationResponse> dailyResponses = new ArrayList<>();

        int totalDemand = 0;
        int fulfilledDemand = 0;
        int totalStockout = 0;
        double inventorySum = 0;

        BigDecimal totalHoldingCost = BigDecimal.ZERO;
        BigDecimal totalStockoutCost = BigDecimal.ZERO;
        BigDecimal totalOrderingCost = BigDecimal.ZERO;

        double averageLeadTime =
                calculation.averageLeadTimeDays();

        double leadTimeStdDev =
                calculation.leadTimeStandardDeviation();

        for (int day = 1; day <= days; day++) {

            int openingInventory = onHand;

            int incoming = incomingSchedule.getOrDefault(day, 0);
            incomingSchedule.remove(day);

            int generatedDemand =
                    demandGenerator.generateDemand(
                            calculation.averageDailyDemand(),
                            calculation.demandStandardDeviation(),
                            demandMultiplier,
                            random);

            DailyInventoryProcessor.DailyResult daily =
                    dailyProcessor.process(
                            onHand,
                            incoming,
                            generatedDemand,
                            backorders);

            onHand = daily.closingInventory();
            backorders = daily.remainingBackorders();

            int unmetDemand = daily.unmetDemand();

            totalDemand += generatedDemand;
            fulfilledDemand += daily.fulfilledToday();
            totalStockout += unmetDemand;
            inventorySum += onHand;

            BigDecimal dailyHoldingCost =
                    holdingCostPerUnit.multiply(
                            BigDecimal.valueOf(onHand));

            BigDecimal dailyStockoutCost =
                    stockoutCostPerUnit.multiply(
                            BigDecimal.valueOf(unmetDemand));

            totalHoldingCost =
                    totalHoldingCost.add(dailyHoldingCost);

            totalStockoutCost =
                    totalStockoutCost.add(dailyStockoutCost);

            // Calculate outstanding purchase quantity.
            int outstanding = incomingSchedule.values()
                    .stream().mapToInt(Integer::intValue).sum();

            int inventoryPosition =
                    onHand + outstanding - backorders;

            boolean shouldOrder = false;
            int quantityToOrder = 0;

            if (policy.getPolicyType()
                    == InventoryPolicy.PolicyType.FIXED_REORDER_POINT) {

                if (inventoryPosition <= reorderPoint) {
                    shouldOrder = true;
                    quantityToOrder = orderQuantity;
                }

            } else if (policy.getPolicyType()
                    == InventoryPolicy.PolicyType.PERIODIC_REVIEW) {

                if (day % reviewInterval == 0) {
                    quantityToOrder =
                            Math.max(0, targetStock - inventoryPosition);

                    shouldOrder = quantityToOrder > 0;
                }
            }

            if (shouldOrder) {

                quantityToOrder = Math.max(
                        quantityToOrder, minimumOrderQuantity);

                int leadDays = leadTimeGenerator.generateLeadTime(
                        averageLeadTime,
                        leadTimeStdDev,
                        leadMultiplier,
                        random);

                int arrivalDay = day + leadDays;

                incomingSchedule.merge(
                        arrivalDay,
                        quantityToOrder,
                        Integer::sum);

                totalOrderingCost =
                        totalOrderingCost.add(orderingCost);
            }

            SimulationResult result = new SimulationResult();

            result.setSimulationRun(run);
            result.setSimulationDay(day);
            result.setOpeningInventory(openingInventory);
            result.setIncomingQuantity(incoming);
            result.setDemandQuantity(generatedDemand);
            result.setFulfilledQuantity(daily.fulfilledToday());
            result.setStockoutQuantity(unmetDemand);
            result.setClosingInventory(onHand);
            result.setHoldingCost(dailyHoldingCost);
            result.setStockoutCost(dailyStockoutCost);
            result.setBackorderQuantity(backorders);

            savedResults.add(result);

            dailyResponses.add(new DailySimulationResponse(
                    day,
                    startDate.plusDays(day - 1),
                    openingInventory,
                    incoming,
                    generatedDemand,
                    daily.fulfilledToday(),
                    unmetDemand,
                    backorders,
                    onHand,
                    dailyHoldingCost,
                    dailyStockoutCost
            ));
        }

        resultRepository.saveAll(savedResults);

        double serviceLevel = totalDemand == 0
                ? 1.0
                : (double) fulfilledDemand / totalDemand;

        double averageInventory = days == 0
                ? 0 : inventorySum / days;

        BigDecimal totalCost = totalHoldingCost
                .add(totalStockoutCost)
                .add(totalOrderingCost);

        run.setStatus(SimulationRun.RunStatus.COMPLETED);
        run.setTotalDemand(totalDemand);
        run.setFulfilledDemand(fulfilledDemand);
        run.setStockoutUnits(totalStockout);
        run.setServiceLevel(serviceLevel);
        run.setAverageInventory(averageInventory);
        run.setTotalHoldingCost(totalHoldingCost);
        run.setTotalStockoutCost(totalStockoutCost);
        run.setTotalOrderingCost(totalOrderingCost);

        runRepository.save(run);

        return new SimulationResponse(
                run.getId(),
                run.getStatus().name(),
                totalDemand,
                fulfilledDemand,
                totalStockout,
                serviceLevel,
                averageInventory,
                totalHoldingCost,
                totalStockoutCost,
                totalOrderingCost,
                totalCost,
                dailyResponses
        );
    }
}
