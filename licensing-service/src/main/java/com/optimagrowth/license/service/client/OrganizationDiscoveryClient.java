package com.optimagrowth.license.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.optimagrowth.license.model.Organization;

@Component
public class OrganizationDiscoveryClient {
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	public Organization getOrganization(String organizationId) {
		RestTemplate restTemplate = new RestTemplate();
		 String uri = discoveryClient.getInstances("organization-service")
		        .stream()
		        .findAny()
		        .map(i -> i.getUri().toString())
		        .orElse(null);
		 if(uri == null) {
			 return null;
		 }
		 
		 String serviceUri = String.format("%s/v1/organization/%s",uri,organizationId);
		 ResponseEntity<Organization> response =  restTemplate.getForEntity(serviceUri, Organization.class);
		 if(response.getStatusCode() == HttpStatus.OK) {
			 return response.getBody();
		 }
		 return null;
	}

}
