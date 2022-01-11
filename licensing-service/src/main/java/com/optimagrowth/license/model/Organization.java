package com.optimagrowth.license.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
	private String id;
	private String name;
	private String contactName;
	private String contactEmail;
	private String contactPhone;
}
