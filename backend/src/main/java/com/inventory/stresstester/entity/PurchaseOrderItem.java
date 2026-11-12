package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.math.BigDecimal;
@Getter @Setter @Entity @Table(name="purchase_order_items")
public class PurchaseOrderItem extends BaseEntity {
    @ManyToOne(optional=false) @JoinColumn(name="purchase_order_id") private PurchaseOrder purchaseOrder;
    @ManyToOne(optional=false) @JoinColumn(name="product_id") private Product product;
    @Column(nullable=false) private Integer orderedQuantity;
    @Column(nullable=false) private Integer receivedQuantity = 0;
    @Column(nullable=false, precision=12, scale=2) private BigDecimal unitPrice;
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getOrderedQuantity() {
		return orderedQuantity;
	}
	public void setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}
	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
}
