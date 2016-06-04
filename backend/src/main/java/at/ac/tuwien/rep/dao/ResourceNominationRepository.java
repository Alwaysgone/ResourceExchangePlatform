package at.ac.tuwien.rep.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import at.ac.tuwien.rep.model.ResourceNomination;

public interface ResourceNominationRepository extends MongoRepository<ResourceNomination, String> {
	/*
	 * SELECT DISTINCT auth FROM Author auth
    WHERE EXISTS
        (SELECT spouseAuthor FROM Author spouseAuthor WHERE spouseAuthor = auth.spouse)
	 */
//	@Query("SELECT DISTINCT n FROM ResourceNomination n WHERE NOT EXISTS"
//			+ " (SELECT ra.nomination FROM ResourceAllocation ra WHERE ra.nomination=n)")
//	List<ResourceNomination> findAllByCustomQuery();
}
