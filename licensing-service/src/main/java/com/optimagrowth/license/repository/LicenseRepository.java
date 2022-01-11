package com.optimagrowth.license.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.license.model.License;

@Repository
public interface LicenseRepository extends CrudRepository<License, java.lang.Long> {

	public List<License> findByorganizationId(String organizationId);

	@Query("from License l where l.organizationId = :organizationId"
			+ " and l.licenseId = :licenseId")
	public Optional<License> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
