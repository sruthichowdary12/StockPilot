package com.inventory.stresstester.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
@Getter @Setter @Entity @Table(name="scenarios")
public class Scenario extends BaseEntity {
    @Column(nullable=false, length=150) private String scenarioName;
    @Column(length=1000) private String description;
    @Column(nullable=false) private Double demandMultiplier = 1.0;
    @Column(nullable=false) private Double leadTimeMultiplier = 1.0;
    @Column(nullable=false) private Integer simulationDays = 30;
    private Long randomSeed;
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
    })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private AppUser createdBy;	public String getScenarioName() {
		return scenarioName;
	}
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getDemandMultiplier() {
		return demandMultiplier;
	}
	public void setDemandMultiplier(Double demandMultiplier) {
		this.demandMultiplier = demandMultiplier;
	}
	public Double getLeadTimeMultiplier() {
		return leadTimeMultiplier;
	}
	public void setLeadTimeMultiplier(Double leadTimeMultiplier) {
		this.leadTimeMultiplier = leadTimeMultiplier;
	}
	public Integer getSimulationDays() {
		return simulationDays;
	}
	public void setSimulationDays(Integer simulationDays) {
		this.simulationDays = simulationDays;
	}
	public Long getRandomSeed() {
		return randomSeed;
	}
	public void setRandomSeed(Long randomSeed) {
		this.randomSeed = randomSeed;
	}
	public AppUser getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(AppUser createdBy) {
		this.createdBy = createdBy;
	}
}
