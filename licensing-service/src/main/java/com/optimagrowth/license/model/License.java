package com.optimagrowth.license.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class License extends RepresentationModel<License> {
	@Id
	@GeneratedValue
	private Long id;
	private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;
    @Transient
    private String comment;
    @Transient
    private String organizationName;
    @Transient
	private String contactName;
    @Transient
	private String contactEmail;
    @Transient
	private String contactPhone;
    
    public License withComment(String comment) {
    	this.setComment(comment);
        return this;
    }
    
    /**
     * A deep clone
     * @param orignal
     * @return
     */
    public License clone(License orignal) {
    	this.description = orignal.description;
    	this.licenseId = orignal.licenseId;
    	this.organizationId = orignal.organizationId;
    	this.productName = orignal.productName;
    	this.licenseType = orignal.licenseType;
    	
    	return this;
    }

}
