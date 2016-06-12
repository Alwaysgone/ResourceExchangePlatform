package at.ac.tuwien.rep;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.rep.dao.ResourceNominationAssociationRepository;
import at.ac.tuwien.rep.dao.ResourceNominationRepository;
import at.ac.tuwien.rep.dao.ResourceRegionRepository;
import at.ac.tuwien.rep.dto.DTOTransformer;
import at.ac.tuwien.rep.dto.ResourceNominationDTO;
import at.ac.tuwien.rep.dto.ResourceRequestDTO;
import at.ac.tuwien.rep.dto.ResourcesDTO;
import at.ac.tuwien.rep.model.ResourceNomination;
import at.ac.tuwien.rep.model.ResourceNominationAssociation;
import at.ac.tuwien.rep.model.ResourceRegion;

@RestController
@RequestMapping(path="/api/")
public class MainController {
	private ResourceNominationRepository nominationRepository;
	private ResourceNominationAssociationRepository nominationAssociationRepository;
	private ResourceRegionRepository regionRepository;
	private DTOTransformer transformer;
	private NominationMatcher nominationMatcher;
	
	@Autowired
	public MainController(ResourceNominationRepository nominationRepository
			, ResourceNominationAssociationRepository nominationAssociationRepository
			, ResourceRegionRepository regionRepository
			, DTOTransformer transformer
			, NominationMatcher nominationMatcher) {
		this.nominationRepository = nominationRepository;
		this.nominationAssociationRepository = nominationAssociationRepository;
		this.regionRepository = regionRepository;
		this.transformer = transformer;
		this.nominationMatcher = nominationMatcher;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getHelloWorld() {
		return "Hello World!";
	}
	
	@RequestMapping(path="/resources", method=RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public String getNumberOfResources(@RequestParam(name="Name", required=false) List<String> resourceNames, @RequestParam(name="Direction", required=false) String direction) {
		Set<String> resources = nominationRepository.findAll().stream().map(n -> n.getResource())
		.filter(r -> resourceNames == null || resourceNames.contains(r)).collect(Collectors.toSet());
		return "Found the following resources: " + (resources != null ? String.join(", ", resources) : "N/A");
	}
	
	@CrossOrigin
	@RequestMapping(path="/nominations", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourcesDTO getNominations() {
		List<ResourceNomination> nominations = nominationRepository.findAll();
		return transformer.transform(nominations);
	}
	
	@CrossOrigin
	@RequestMapping(path="/nominations/{nominationId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourceNominationDTO getNomination(@PathVariable("nominationId") Long nominationId) {
		ResourceNomination nomination = nominationRepository.getOne(nominationId);
		if(nomination == null) {
			//TODO throw exception for 404
		}
		//TODO fix missing matchedNominations list
		return transformer.transform(nomination);
	}
	
	@CrossOrigin
	@RequestMapping(path="/nominations", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public List<Long> addNominations(@RequestBody ResourceRequestDTO request) {
		if(request == null
				|| request.getParticipant() == null
				|| request.getNominations() == null
				|| request.getNominations().isEmpty()) {
			throw new IllegalArgumentException("Either request, participant or nominations was null!");
		}
		for (ResourceNominationDTO nominationDto : request.getNominations()) {
			if(nominationDto.getResource() == null
					|| nominationDto.getQuantity() == null
					|| nominationDto.getUnit() == null
					|| nominationDto.getDirection() == null) {
				throw new IllegalArgumentException("Either resource, quantity, unit or direction was null in nomination!");
			}
		}
		ResourceNominationAssociation association = new ResourceNominationAssociation();
		association.setParticipant(request.getParticipant());
		association.setNominations(request.getNominations().stream().map(n -> transformer.transform(n)).collect(Collectors.toList()));
		final ResourceNominationAssociation managedAssociation = nominationAssociationRepository.save(association);
		List<Long> ids = association.getNominations().stream().map(n -> {n.setAssociation(managedAssociation); n.setMatchedNominations(new ArrayList<>()); return n;}).map(n -> nominationRepository.save(n).getId()).collect(Collectors.toList());
//		Thread matchingThread = new Thread(new NominationMatcher.NominationMatcherThread(nominationMatcher));
//		matchingThread.start();
		new NominationMatcher.NominationMatcherThread(nominationMatcher).run();
		return ids;
	}
	
	@CrossOrigin
	@RequestMapping(path="/regions", method=RequestMethod.GET)
	public List<String> getResourceRegions() {
		return regionRepository.findAll().stream().map(r -> r.getName()).collect(Collectors.toList());
	}
	
	@RequestMapping(path="/regions", method=RequestMethod.POST)
	public Long createRegion(@RequestParam("name") String regionName) {
		if(regionName == null) {
			throw new IllegalArgumentException("Region name cannot be null!");
		}
		ResourceRegion region = new ResourceRegion();
		region.setName(regionName);
		return regionRepository.save(region).getId();
	}
	
	@RequestMapping(path="/resources/{resourceId}", method= RequestMethod.POST, consumes={"application/xml", "application/rdf+xml"})
	public String addResourceAllocations(@PathParam("resourceId") String resourceId) {
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
