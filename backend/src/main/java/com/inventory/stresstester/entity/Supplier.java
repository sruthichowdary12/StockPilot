package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
@Getter @Setter @Entity @Table(name="suppliers")
public class Supplier extends BaseEntity {
    @Column(name="supplier_name", nullable=false, length=150) private String supplierName;
    @Column(name="contact_email", length=150) private String contactEmail;
    @Column(name="contact_number", length=40) private String contactNumber;
    private Integer defaultLeadTimeDays;
    private Double leadTimeStdDevDays;
    private Double supplierRating;
    @Column(nullable=false) private Boolean active = true;
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Integer getDefaultLeadTimeDays() {
		return defaultLeadTimeDays;
	}
	public void setDefaultLeadTimeDays(Integer defaultLeadTimeDays) {
		this.defaultLeadTimeDays = defaultLeadTimeDays;
	}
	public Double getLeadTimeStdDevDays() {
		return leadTimeStdDevDays;
	}
	public void setLeadTimeStdDevDays(Double leadTimeStdDevDays) {
		this.leadTimeStdDevDays = leadTimeStdDevDays;
	}
	public Double getSupplierRating() {
		return supplierRating;
	}
	public void setSupplierRating(Double supplierRating) {
		this.supplierRating = supplierRating;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
}
