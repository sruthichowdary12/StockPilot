package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.math.BigDecimal;
@Getter @Setter @Entity @Table(name="simulation_results")
public class SimulationResult extends BaseEntity {
    @ManyToOne(optional=false) @JoinColumn(name="simulation_run_id") private SimulationRun simulationRun;
    @Column(nullable=false) private Integer simulationDay;
    private Integer openingInventory;
    private Integer demandQuantity;
    private Integer fulfilledQuantity;
    private Integer stockoutQuantity;
    private Integer incomingQuantity;
    private Integer closingInventory;
    @Column(precision=14, scale=2) private BigDecimal holdingCost;
    @Column(precision=14, scale=2) private BigDecimal stockoutCost;
    @Column(nullable = false)
    private Integer backorderQuantity = 0;
	public SimulationRun getSimulationRun() {
		return simulationRun;
	}
	public void setSimulationRun(SimulationRun simulationRun) {
		this.simulationRun = simulationRun;
	}
	public Integer getSimulationDay() {
		return simulationDay;
	}
	public void setSimulationDay(Integer simulationDay) {
		this.simulationDay = simulationDay;
	}
	public Integer getOpeningInventory() {
		return openingInventory;
	}
	public void setOpeningInventory(Integer openingInventory) {
		this.openingInventory = openingInventory;
	}
	public Integer getDemandQuantity() {
		return demandQuantity;
	}
	public void setDemandQuantity(Integer demandQuantity) {
		this.demandQuantity = demandQuantity;
	}
	public Integer getFulfilledQuantity() {
		return fulfilledQuantity;
	}
	public void setFulfilledQuantity(Integer fulfilledQuantity) {
		this.fulfilledQuantity = fulfilledQuantity;
	}
	public Integer getStockoutQuantity() {
		return stockoutQuantity;
	}
	public void setStockoutQuantity(Integer stockoutQuantity) {
		this.stockoutQuantity = stockoutQuantity;
	}
	public Integer getIncomingQuantity() {
		return incomingQuantity;
	}
	public void setIncomingQuantity(Integer incomingQuantity) {
		this.incomingQuantity = incomingQuantity;
	}
	public Integer getClosingInventory() {
		return closingInventory;
	}
	public void setClosingInventory(Integer closingInventory) {
		this.closingInventory = closingInventory;
	}
	public BigDecimal getHoldingCost() {
		return holdingCost;
	}
	public void setHoldingCost(BigDecimal holdingCost) {
		this.holdingCost = holdingCost;
	}
	public BigDecimal getStockoutCost() {
		return stockoutCost;
	}
	public void setStockoutCost(BigDecimal stockoutCost) {
		this.stockoutCost = stockoutCost;
	}
	/**
	 * @return the backorderQuantity
	 */
	public Integer getBackorderQuantity() {
		return backorderQuantity;
	}
	/**
	 * @param backorderQuantity the backorderQuantity to set
	 */
	public void setBackorderQuantity(Integer backorderQuantity) {
		this.backorderQuantity = backorderQuantity;
	}
}
