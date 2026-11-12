package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
@Getter @Setter @Entity @Table(name="inventory_policies")
public class InventoryPolicy extends BaseEntity {
    public enum PolicyType { FIXED_REORDER_POINT, PERIODIC_REVIEW }
    @Column(nullable=false, length=120) private String policyName;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=40) private PolicyType policyType;
    @ManyToOne(optional=false) @JoinColumn(name="product_id") private Product product;
    private Integer reorderPoint;
    private Integer orderQuantity;
    private Integer reviewIntervalDays;
    private Integer targetStockLevel;
    private Integer safetyStock;
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public PolicyType getPolicyType() {
		return policyType;
	}
	public void setPolicyType(PolicyType policyType) {
		this.policyType = policyType;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getReorderPoint() {
		return reorderPoint;
	}
	public void setReorderPoint(Integer reorderPoint) {
		this.reorderPoint = reorderPoint;
	}
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public Integer getReviewIntervalDays() {
		return reviewIntervalDays;
	}
	public void setReviewIntervalDays(Integer reviewIntervalDays) {
		this.reviewIntervalDays = reviewIntervalDays;
	}
	public Integer getTargetStockLevel() {
		return targetStockLevel;
	}
	public void setTargetStockLevel(Integer targetStockLevel) {
		this.targetStockLevel = targetStockLevel;
	}
	public Integer getSafetyStock() {
		return safetyStock;
	}
	public void setSafetyStock(Integer safetyStock) {
		this.safetyStock = safetyStock;
	}
}
