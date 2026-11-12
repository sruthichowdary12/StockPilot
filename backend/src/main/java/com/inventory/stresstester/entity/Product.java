package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.math.BigDecimal;
@Getter @Setter @Entity @Table(name="products")
public class Product extends BaseEntity {
    @Column(name="sku_code", nullable=false, unique=true, length=60) private String skuCode;
    public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public BigDecimal getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}
	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public BigDecimal getHoldingCostPerUnitDay() {
		return holdingCostPerUnitDay;
	}
	public void setHoldingCostPerUnitDay(BigDecimal holdingCostPerUnitDay) {
		this.holdingCostPerUnitDay = holdingCostPerUnitDay;
	}
	public BigDecimal getStockoutCostPerUnit() {
		return stockoutCostPerUnit;
	}
	public void setStockoutCostPerUnit(BigDecimal stockoutCostPerUnit) {
		this.stockoutCostPerUnit = stockoutCostPerUnit;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	@Column(name="product_name", nullable=false, length=150) private String productName;
    @Column(length=100) private String category;
    @Column(precision=12, scale=2) private BigDecimal unitCost;
    @Column(precision=12, scale=2) private BigDecimal sellingPrice;
    @Column(precision=12, scale=2) private BigDecimal holdingCostPerUnitDay;
    @Column(precision=12, scale=2) private BigDecimal stockoutCostPerUnit;
    @Column(nullable=false) private Boolean active = true;
}
