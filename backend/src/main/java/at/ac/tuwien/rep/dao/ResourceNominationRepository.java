package at.ac.tuwien.rep.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.tuwien.rep.model.ResourceNomination;

public interface ResourceNominationRepository extends JpaRepository<ResourceNomination, Long> {
//	List<ResourceNomination> findByIdIn(List<String> ids);
	
//	@Query("select n from ResourceNomination n WHERE n.fulfilledNominations IS NOT EMPTY")
//	List<ResourceNomination> findAllWithNoAllocation();
	
	@Query("SELECT n FROM ResourceNomination n ORDER BY n.resource ASC, n.quantity DESC")
	Page<ResourceNomination> findAll(Pageable pageable);
	
	@Query("SELECT n FROM ResourceNomination n WHERE"
			+ " LOWER(n.region.name) LIKE CONCAT('%',LOWER(:region),'%')"
			+ " ORDER BY n.resource, n.quantity DESC")
	Page<ResourceNomination> findByRegionContainingIgnoreCaseOrderByResource(@Param("region")String region, Pageable pageable);
	
	@Query("SELECT n FROM ResourceNomination n WHERE"
			+ " LOWER(n.resource) LIKE CONCAT('%',LOWER(:resource),'%')"
			+ " ORDER BY n.resource, n.quantity DESC")
	Page<ResourceNomination> findByResourceContainingIgnoreCaseOrderByResource(@Param("resource")String resource, Pageable pageable);
	
	@Query("SELECT n FROM ResourceNomination n WHERE"
			+ " LOWER(n.region.name) LIKE CONCAT('%',LOWER(:region),'%')"
			+ " AND LOWER(n.resource) LIKE CONCAT('%',LOWER(:resource),'%')"
			+ " ORDER BY n.resource, n.quantity DESC")
	Page<ResourceNomination> findByRegionContainingIgnoreCaseAndResourceContainingIgnoreCaseOrderByResource(@Param("region")String region, @Param("resource")String resource, Pageable pageable);
}
