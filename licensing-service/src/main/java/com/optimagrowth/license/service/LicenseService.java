package com.optimagrowth.license.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
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
	private OrganizationFeignClient organizationFeignClient;

	public License getLicense(String licenseId, String organizationId, String clientType) {

		log.debug("Get license by organization");
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

	@CircuitBreaker(name = "licenseService", fallbackMethod= "buildFallbackLicenseList")
	@Retry(name = "retryLicenseService" , fallbackMethod = "buildFallbackLicenseList")
	@Bulkhead(name = "bulkheadLicenseService", fallbackMethod= "buildFallbackLicenseList")
	@RateLimiter(name = "licenseService", fallbackMethod= "buildFallbackLicenseList")
	public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
		log.debug("Get License by Organization");
		randomlyRunLong();
		return licenseRepository.findByorganizationId(organizationId);
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

	@CircuitBreaker(name = "organizationService")
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

	private void randomlyRunLong() throws TimeoutException {
		Random rand = new Random();
		int randomNum = rand.nextInt((3 - 1) + 1) + 1;
		if (randomNum == 3)
			sleep();
	}

	private void sleep() throws TimeoutException {
		try {
			System.out.println("Sleep");
			Thread.sleep(5000);
			throw new java.util.concurrent.TimeoutException();
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}

	private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
		List<License> fallbackList = new ArrayList<>();
		License license = new License();
		license.setLicenseId("0000000-00-00000");
		license.setOrganizationId(organizationId);
		license.setProductName("Sorry no licensing information currently available");
		fallbackList.add(license);
		return fallbackList;
	}

}
