package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

@RestController
@RequestMapping("/v1/organization/{organizationId}/license")
public class LicenseController {
	
	@Autowired
	private LicenseService licenseService;
	
	@GetMapping("/{licenseId}/{clientType}")
	private License getLicense(
			@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId,
			@PathVariable("clientType") String clientType) {
		
		License license = licenseService.getLicense(licenseId, organizationId, clientType);
		
		 license.add(linkTo(LicenseController.class, license.getOrganizationId())
				   .slash(license.getLicenseId())
				   .withSelfRel());
		 license.add(linkTo(LicenseController.class, license.getOrganizationId())
				   .withRel("createLicense"));
		 license.add(linkTo(LicenseController.class, license.getOrganizationId())
				   .withRel("updateLicense"));
		 license.add(linkTo(LicenseController.class, license.getOrganizationId())
				 .slash(license.getLicenseId())
				   .withRel("deleteLicense"));
		        
		return license;
	}
	
	@PostMapping
	private License createLicense(
			@PathVariable("organizationId") String organizationId,
			@RequestBody License license
			) {
		 return licenseService.createLicense(license);
	}
	
	@PutMapping
	private License updateLicense(
			@PathVariable("organizationId") String organizationId,
			@RequestBody License license
			) {
		 return licenseService.updateLicense(license);
	}
	
	@DeleteMapping("/{licenseId}")
	private String deleteLicense(
	          @PathVariable("organizationId") String organizationId,
	          @PathVariable("licenseId") String licenseId) {
		return licenseService.deleteLicense(licenseId, organizationId);
	}
}
