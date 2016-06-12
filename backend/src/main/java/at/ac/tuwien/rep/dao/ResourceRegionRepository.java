package at.ac.tuwien.rep.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.tuwien.rep.model.ResourceRegion;

public interface ResourceRegionRepository extends JpaRepository<ResourceRegion, Long> {
	ResourceRegion findByName(String name);
}
