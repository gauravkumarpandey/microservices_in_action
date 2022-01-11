package com.optimagrowth.license.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LicenseService {

	@Autowired
	private LicenseRepository licenseRepository;
	@Autowired
	private ServiceConfig serviceConfig;
	@Autowired
	private OrganizationDiscoveryClient organizationDiscoveryClient;
	@Autowired
	private OrganizationRestTemplateClient organizationRestTemplateClient;
	@Autowired
	private OrganizationFeignClient  organizationFeignClient;
	
	public License getLicense(String licenseId, String organizationId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
				.orElseThrow(() -> new IllegalArgumentException(String.format(
						"Unable to find license with id: %s and organization id: %s", licenseId, organizationId)));

		Organization organization = retrieveOrganizationInfo(organizationId, clientType);
		if (organization != null) {
			license.setOrganizationName(organization.getName());
			license.setContactName(organization.getContactName());
			license.setContactEmail(organization.getContactEmail());
			license.setContactPhone(organization.getContactPhone());
		}
		return license.withComment(serviceConfig.getProperty());
	}

	public License createLicense(License license) {
		License savedLicense = licenseRepository.save(license);
		return savedLicense.withComment(serviceConfig.getProperty());
	}

	public License updateLicense(License license) {
		License found = licenseRepository
				.findByOrganizationIdAndLicenseId(license.getOrganizationId(), license.getLicenseId())
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Unable to find license with id: %s and organization id: %s",
								license.getLicenseId(), license.getOrganizationId())));

		found.clone(license);
		License savedLicense = licenseRepository.save(found);
		return savedLicense.withComment(serviceConfig.getProperty());
	}

	public String deleteLicense(String licenseId, String organizationId) {

		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
				.orElseThrow(() -> new IllegalArgumentException(String.format(
						"Unable to find license with id: %s and organization id: %s", licenseId, organizationId)));

		licenseRepository.delete(license);
		return String.format("Deleted license with id %s for the organization %s", licenseId, organizationId);
	}
	
	
	private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
		switch (clientType) {
		case "discovery":
			 log.info("I am using Eureka discovery client");
			return organizationDiscoveryClient.getOrganization(organizationId);
		case "rest":
			log.info("I am using load balanced rest template");
		   return organizationRestTemplateClient.getOrganization(organizationId);
		case "feign":
			log.info("I am using Netflix Feigh client");
			return organizationFeignClient.getOrganization(organizationId);
		default:
			break;
		}
		
		return null;
	}


}
