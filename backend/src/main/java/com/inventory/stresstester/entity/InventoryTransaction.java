package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.LocalDateTime;
@Getter @Setter @Entity @Table(name="inventory_transactions")
public class InventoryTransaction extends BaseEntity {
    public enum TransactionType { PURCHASE_RECEIVED, CUSTOMER_SALE, STOCK_ADJUSTMENT, RETURN, BACKORDER_FULFILLED }
    @ManyToOne(optional=false) @JoinColumn(name="product_id") private Product product;
    @Enumerated(EnumType.STRING) @Column(name="transaction_type", nullable=false, length=40)
    private TransactionType transactionType;
    @Column(nullable=false) private Integer quantity;
    @Column(name="transaction_date", nullable=false) private LocalDateTime transactionDate;
    @Column(length=100) private String referenceId;
    @Column(length=500) private String remarks;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
