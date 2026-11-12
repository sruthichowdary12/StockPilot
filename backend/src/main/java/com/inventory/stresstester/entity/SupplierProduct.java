package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.math.BigDecimal;
@Getter @Setter @Entity @Table(name="supplier_products",
    uniqueConstraints=@UniqueConstraint(columnNames={"supplier_id","product_id"}))
public class SupplierProduct extends BaseEntity {
    @ManyToOne(optional=false) @JoinColumn(name="supplier_id") private Supplier supplier;
    @ManyToOne(optional=false) @JoinColumn(name="product_id") private Product product;
    @Column(nullable=false) private Integer minimumOrderQuantity;
    @Column(precision=12, scale=2) private BigDecimal unitPurchasePrice;
    private Integer leadTimeDays;
    @Column(nullable=false) private Boolean preferredSupplier = false;
	/**
	 * @return the supplier
	 */
	public Supplier getSupplier() {
		return supplier;
	}
	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	/**
	 * @return the minimumOrderQuantity
	 */
	public Integer getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}
	/**
	 * @param minimumOrderQuantity the minimumOrderQuantity to set
	 */
	public void setMinimumOrderQuantity(Integer minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}
	/**
	 * @return the unitPurchasePrice
	 */
	public BigDecimal getUnitPurchasePrice() {
		return unitPurchasePrice;
	}
	/**
	 * @param unitPurchasePrice the unitPurchasePrice to set
	 */
	public void setUnitPurchasePrice(BigDecimal unitPurchasePrice) {
		this.unitPurchasePrice = unitPurchasePrice;
	}
	/**
	 * @return the leadTimeDays
	 */
	public Integer getLeadTimeDays() {
		return leadTimeDays;
	}
	/**
	 * @param leadTimeDays the leadTimeDays to set
	 */
	public void setLeadTimeDays(Integer leadTimeDays) {
		this.leadTimeDays = leadTimeDays;
	}
	/**
	 * @return the preferredSupplier
	 */
	public Boolean getPreferredSupplier() {
		return preferredSupplier;
	}
	/**
	 * @param preferredSupplier the preferredSupplier to set
	 */
	public void setPreferredSupplier(Boolean preferredSupplier) {
		this.preferredSupplier = preferredSupplier;
	}
}
