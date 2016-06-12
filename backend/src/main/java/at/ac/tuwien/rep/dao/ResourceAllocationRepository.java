package at.ac.tuwien.rep.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import at.ac.tuwien.rep.model.ResourceAllocation;

public interface ResourceAllocationRepository extends JpaRepository<ResourceAllocation, Long> {
	@Query("Select a FROM ResourceAllocation a WHERE a.nomination.id=:nominationId")
	ResourceAllocation findByNominationId(Long nominationId);
}
