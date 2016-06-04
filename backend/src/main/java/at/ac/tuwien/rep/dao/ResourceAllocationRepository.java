package at.ac.tuwien.rep.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import at.ac.tuwien.rep.model.ResourceAllocation;

public interface ResourceAllocationRepository extends MongoRepository<ResourceAllocation, String> {

}
