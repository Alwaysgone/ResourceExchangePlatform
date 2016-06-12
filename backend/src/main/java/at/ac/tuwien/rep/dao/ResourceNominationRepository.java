package at.ac.tuwien.rep.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import at.ac.tuwien.rep.model.ResourceNomination;

public interface ResourceNominationRepository extends JpaRepository<ResourceNomination, Long> {
	List<ResourceNomination> findByIdIn(List<String> ids);
	
	@Query("select n from ResourceNomination n WHERE n.allocation IS NULL")
	List<ResourceNomination> findAllWithNoAllocation();
}
