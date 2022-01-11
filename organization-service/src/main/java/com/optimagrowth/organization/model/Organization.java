package com.optimagrowth.organization.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
public class Organization extends RepresentationModel<Organization> {
	
	@Id
    @Column(name = "organization_id", nullable = false)
    private String id;
	private String name;
	private String contactName;
	private String contactEmail;
	private String contactPhone;
}
