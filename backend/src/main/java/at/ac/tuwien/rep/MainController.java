package at.ac.tuwien.rep;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.rep.dao.ResourceAllocationRepository;
import at.ac.tuwien.rep.dao.ResourceNominationRepository;
import at.ac.tuwien.rep.dao.ResourceRepository;
import at.ac.tuwien.rep.dto.DTOTransformer;
import at.ac.tuwien.rep.dto.ResourcesDTO;

@RestController
@RequestMapping(path="/")
public class MainController {
	
	private ResourceRepository resourceRepository;
	private ResourceNominationRepository resourceNominationRepository;
	private ResourceAllocationRepository resourceAllocationRepository;
	
	@Autowired
	public MainController(ResourceRepository resourceRepository, ResourceNominationRepository resourceNominationRepository
			, ResourceAllocationRepository resourceAllocationRepository) {
		this.resourceRepository = resourceRepository;
		this.resourceNominationRepository = resourceNominationRepository;
		this.resourceAllocationRepository = resourceAllocationRepository;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getHelloWorld() {
		return "Hello World!";
	}
	
	@RequestMapping(path="/resources", method=RequestMethod.GET)
	public String getNumberOfResources(@RequestParam(name="Name", required=false) List<String> resourceNames, @RequestParam(name="Direction", required=false) String direction) {
		return "Found " + resourceRepository.findByNameIn(resourceNames).size() + " resources with name in " + String.join(", ", resourceNames);
	}
	
	@RequestMapping(path="/nominations", method=RequestMethod.GET)
	public ResourcesDTO getNominations() {
		return DTOTransformer.transform(resourceNominationRepository.findAllByCustomQuery(), resourceAllocationRepository.findAll());
	}
	
	@RequestMapping(path="/resources", method= RequestMethod.POST, consumes={"application/xml", "application/rdf+xml"})
	public String addResourceAllocations() {
		return "";
	}
	
	@RequestMapping(value="/rdf", method=RequestMethod.GET, produces={"application/xml", "application/rdf+xml"})
    public String getModelAsXml() {
       // Note that we added "application/rdf+xml" as one of the supported types
       // for this method. Otherwise, we utilize your existing xml serialization
		return "";
    }
	
	/*
	 * <?xml version="1.0"?>
<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:dc="http://purl.org/dc/elements/1.1/">
  <rdf:Description rdf:about="http://www.w3.org/">
    <dc:title>World Wide Web Consortium</dc:title> 
  </rdf:Description>
</rdf:RDF>
  
	 */
}
