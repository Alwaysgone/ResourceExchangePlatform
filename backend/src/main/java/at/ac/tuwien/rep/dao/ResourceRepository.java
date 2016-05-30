package at.ac.tuwien.rep.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import at.ac.tuwien.rep.model.Resource;

public interface ResourceRepository extends MongoRepository<Resource, String> {
	List<Resource> findByName(String name);
}
