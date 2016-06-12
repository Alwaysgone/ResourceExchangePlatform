package at.ac.tuwien.rep.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.tuwien.rep.model.ResourceNomination;

public interface ResourceNominationRepository extends JpaRepository<ResourceNomination, Long> {
//	List<ResourceNomination> findByIdIn(List<String> ids);
	
//	@Query("select n from ResourceNomination n WHERE n.fulfilledNominations IS NOT EMPTY")
//	List<ResourceNomination> findAllWithNoAllocation();
}
