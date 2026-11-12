package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
@Getter @Setter @Entity @Table(name="inventory",
    uniqueConstraints=@UniqueConstraint(columnNames={"product_id","warehouse_code"}))
public class Inventory extends BaseEntity {
    @ManyToOne(optional=false) @JoinColumn(name="product_id") private Product product;
    @Column(name="warehouse_code", nullable=false, length=60) private String warehouseCode;
    @Column(nullable=false) private Integer quantityOnHand = 0;
    @Column(nullable=false) private Integer quantityOnOrder = 0;
    @Column(nullable=false) private Integer backorderQuantity = 0;
    @Column(nullable=false) private Integer safetyStock = 0;
    @Column(nullable=false) private Integer reorderPoint = 0;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public Integer getQuantityOnHand() {
		return quantityOnHand;
	}
	public void setQuantityOnHand(Integer quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}
	public Integer getQuantityOnOrder() {
		return quantityOnOrder;
	}
	public void setQuantityOnOrder(Integer quantityOnOrder) {
		this.quantityOnOrder = quantityOnOrder;
	}
	public Integer getBackorderQuantity() {
		return backorderQuantity;
	}
	public void setBackorderQuantity(Integer backorderQuantity) {
		this.backorderQuantity = backorderQuantity;
	}
	public Integer getSafetyStock() {
		return safetyStock;
	}
	public void setSafetyStock(Integer safetyStock) {
		this.safetyStock = safetyStock;
	}
	public Integer getReorderPoint() {
		return reorderPoint;
	}
	public void setReorderPoint(Integer reorderPoint) {
		this.reorderPoint = reorderPoint;
	}
}
