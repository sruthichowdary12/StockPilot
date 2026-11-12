package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
@Getter @Setter @Entity @Table(name="simulation_runs")
public class SimulationRun extends BaseEntity {
    public enum RunStatus { PENDING, RUNNING, COMPLETED, FAILED }
    @ManyToOne(optional=false) @JoinColumn(name="scenario_id") private Scenario scenario;
    @ManyToOne(optional=false) @JoinColumn(name="policy_id") private InventoryPolicy policy;
    @ManyToOne(optional=false) @JoinColumn(name="product_id") private Product product;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private RunStatus status = RunStatus.PENDING;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long randomSeed;
    private Integer totalDemand;
    private Integer fulfilledDemand;
    private Integer stockoutUnits;
    private Double serviceLevel;
    private Double averageInventory;
    @Column(precision=14, scale=2) private BigDecimal totalHoldingCost;
    @Column(precision=14, scale=2) private BigDecimal totalStockoutCost;
    @Column(precision=14, scale=2) private BigDecimal totalOrderingCost;
	public Scenario getScenario() {
		return scenario;
	}
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}
	public InventoryPolicy getPolicy() {
		return policy;
	}
	public void setPolicy(InventoryPolicy policy) {
		this.policy = policy;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public RunStatus getStatus() {
		return status;
	}
	public void setStatus(RunStatus status) {
		this.status = status;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public Long getRandomSeed() {
		return randomSeed;
	}
	public void setRandomSeed(Long randomSeed) {
		this.randomSeed = randomSeed;
	}
	public Integer getTotalDemand() {
		return totalDemand;
	}
	public void setTotalDemand(Integer totalDemand) {
		this.totalDemand = totalDemand;
	}
	public Integer getFulfilledDemand() {
		return fulfilledDemand;
	}
	public void setFulfilledDemand(Integer fulfilledDemand) {
		this.fulfilledDemand = fulfilledDemand;
	}
	public Integer getStockoutUnits() {
		return stockoutUnits;
	}
	public void setStockoutUnits(Integer stockoutUnits) {
		this.stockoutUnits = stockoutUnits;
	}
	public Double getServiceLevel() {
		return serviceLevel;
	}
	public void setServiceLevel(Double serviceLevel) {
		this.serviceLevel = serviceLevel;
	}
	public Double getAverageInventory() {
		return averageInventory;
	}
	public void setAverageInventory(Double averageInventory) {
		this.averageInventory = averageInventory;
	}
	public BigDecimal getTotalHoldingCost() {
		return totalHoldingCost;
	}
	public void setTotalHoldingCost(BigDecimal totalHoldingCost) {
		this.totalHoldingCost = totalHoldingCost;
	}
	public BigDecimal getTotalStockoutCost() {
		return totalStockoutCost;
	}
	public void setTotalStockoutCost(BigDecimal totalStockoutCost) {
		this.totalStockoutCost = totalStockoutCost;
	}
	public BigDecimal getTotalOrderingCost() {
		return totalOrderingCost;
	}
	public void setTotalOrderingCost(BigDecimal totalOrderingCost) {
		this.totalOrderingCost = totalOrderingCost;
	}
}
