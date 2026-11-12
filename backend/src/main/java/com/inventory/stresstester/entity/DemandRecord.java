package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.LocalDate;
@Getter @Setter @Entity @Table(name="demand_records",
    uniqueConstraints=@UniqueConstraint(columnNames={"product_id","demand_date"}))
public class DemandRecord extends BaseEntity {
    @ManyToOne(optional=false) @JoinColumn(name="product_id") private Product product;
    @Column(name="demand_date", nullable=false) private LocalDate demandDate;
    @Column(nullable=false) private Integer demandQuantity;
    private Double seasonalityFactor;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public LocalDate getDemandDate() {
		return demandDate;
	}
	public void setDemandDate(LocalDate demandDate) {
		this.demandDate = demandDate;
	}
	public Integer getDemandQuantity() {
		return demandQuantity;
	}
	public void setDemandQuantity(Integer demandQuantity) {
		this.demandQuantity = demandQuantity;
	}
	public Double getSeasonalityFactor() {
		return seasonalityFactor;
	}
	public void setSeasonalityFactor(Double seasonalityFactor) {
		this.seasonalityFactor = seasonalityFactor;
	}
}
