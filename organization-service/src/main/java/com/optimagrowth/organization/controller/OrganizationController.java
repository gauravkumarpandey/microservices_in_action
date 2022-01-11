package com.optimagrowth.organization.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.service.OrganizationService;

@RestController
@RequestMapping("/v1/organization")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;

	@GetMapping("/{organizationId}")
	private Organization getOrganization(@PathVariable("organizationId") String organizationId) {

		Organization organization = organizationService.findById(organizationId);

		organization.add(linkTo(OrganizationController.class).slash(organization.getId())
				.withSelfRel());
		organization.add(linkTo(OrganizationController.class).withRel("createOrganization"));
		organization.add(linkTo(OrganizationController.class).withRel("updateOrganization"));
		organization.add(linkTo(OrganizationController.class).slash(organization.getId())
				.withRel("deleteOrganization"));

		return organization;
	}

	@PostMapping
	private Organization createOrganization(@RequestBody Organization organization) {
		return organizationService.create(organization);
	}

	@PutMapping
	private Organization updateOrganization(@RequestBody Organization organization) {
		return organizationService.update(organization);
	}

	@DeleteMapping("/{organizationId}")
	private void deleteOrganization(@PathVariable("organizationId") String organizationId) {
		organizationService.delete(organizationId);
	}
}
