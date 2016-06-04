package at.ac.tuwien.rep.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import at.ac.tuwien.rep.model.ResourceNominationAssociation;

public interface ResourceNominationAssociationRepository extends MongoRepository<ResourceNominationAssociation, String> {

}
