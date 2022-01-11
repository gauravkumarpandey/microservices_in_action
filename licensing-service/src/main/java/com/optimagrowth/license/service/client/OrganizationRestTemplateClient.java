package com.optimagrowth.license.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.optimagrowth.license.model.Organization;

@Component
public class OrganizationRestTemplateClient {

	@Autowired
	private RestTemplate restTemplate;
	
	public Organization getOrganization(String organizationId) {
	   ResponseEntity<Organization> response =restTemplate.exchange("http://organization-service/v1/organization/{organizationId}",
			      HttpMethod.GET,
			      null,
				 Organization.class,
				 organizationId);
	   
	   if(response.getStatusCode() == HttpStatus.OK) {
		   return response.getBody();
	   }
	   
	   return null;
	}
}
