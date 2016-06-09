package at.ac.tuwien.rep;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.rep.dao.ResourceAllocationRepository;
import at.ac.tuwien.rep.dao.ResourceNominationAssociationRepository;
import at.ac.tuwien.rep.dao.ResourceNominationRepository;
import at.ac.tuwien.rep.dto.DTOTransformer;
import at.ac.tuwien.rep.dto.ResourceAllocationDTO;
import at.ac.tuwien.rep.dto.ResourceRequestDTO;
import at.ac.tuwien.rep.dto.ResourcesDTO;
import at.ac.tuwien.rep.model.ResourceAllocation;
import at.ac.tuwien.rep.model.ResourceNomination;
import at.ac.tuwien.rep.model.ResourceNominationAssociation;

@RestController
@CrossOrigin
@RequestMapping(path="/api/")
public class MainController {
	private ResourceNominationRepository resourceNominationRepository;
	private ResourceAllocationRepository resourceAllocationRepository;
	private ResourceNominationAssociationRepository resourceNominationAssociationRepository;
	
	@Autowired
	public MainController(ResourceNominationRepository resourceNominationRepository
			, ResourceAllocationRepository resourceAllocationRepository
			, ResourceNominationAssociationRepository resourceNominationAssociationRepository) {
		this.resourceNominationRepository = resourceNominationRepository;
		this.resourceAllocationRepository = resourceAllocationRepository;
		this.resourceNominationAssociationRepository = resourceNominationAssociationRepository;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getHelloWorld() {
		return "Hello World!";
	}
	
	@RequestMapping(path="/resources", method=RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public String getNumberOfResources(@RequestParam(name="Name", required=false) List<String> resourceNames, @RequestParam(name="Direction", required=false) String direction) {
		Set<String> resources = resourceNominationRepository.findAll().stream().map(n -> n.getResource())
		.filter(r -> resourceNames == null || resourceNames.contains(r)).collect(Collectors.toSet());
		return "Found the following resources: " + (resources != null ? String.join(", ", resources) : "N/A");
	}
	
	@RequestMapping(path="/nominations", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourcesDTO getNominations() {
		List<ResourceAllocation> allocations = resourceAllocationRepository.findAll();
		List<ResourceNomination> nominations = resourceNominationRepository.findAll().stream()
				.filter(n -> !allocations.stream().map(a -> a.getNomination())
						.collect(Collectors.toList()).contains(n)).collect(Collectors.toList());
		return DTOTransformer.transform(nominations, allocations);
	}
	
	@RequestMapping(path="/nominations/{nominationId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceAllocationDTO getAllocation(@PathVariable("nominationId") String nominationId) {
		List<ResourceAllocationDTO> allocations = resourceAllocationRepository.findAll().stream().filter(a -> a.getNomination().getId().equals(nominationId))
				.map(a -> DTOTransformer.transform(a)).collect(Collectors.toList());
		if(allocations.size() > 1) {
			throw new IllegalStateException("");
		}
		return allocations.isEmpty() ? null : allocations.get(0);
	}
	
	@RequestMapping(path="/nominations", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public List<String> addNominations(@RequestBody ResourceRequestDTO request) {
		ResourceNominationAssociation association = new ResourceNominationAssociation();
		association.setParticipant(request.getParticipant());
		association.setNominations(request.getNominations().stream().map(n -> DTOTransformer.transform(n)).collect(Collectors.toList()));
		association.setNominations(association.getNominations().stream().map(n -> resourceNominationRepository.save(n)).collect(Collectors.toList()));
		association = resourceNominationAssociationRepository.save(association);
		return association.getNominations().stream().map(n -> n.getId()).collect(Collectors.toList());
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
