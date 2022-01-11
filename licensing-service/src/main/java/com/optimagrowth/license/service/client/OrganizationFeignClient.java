package com.optimagrowth.license.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.optimagrowth.license.model.Organization;

@FeignClient("organization-service")
public interface OrganizationFeignClient {
	
	@GetMapping("/v1/organization/{organizationId}")
	Organization getOrganization(@PathVariable("organizationId") String organizationId);

}
