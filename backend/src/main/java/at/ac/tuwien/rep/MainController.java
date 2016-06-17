package at.ac.tuwien.rep;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.sf.dynamicreports.report.exception.DRException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import at.ac.tuwien.rep.report.Report;
import at.ac.tuwien.rep.report.ReportGenerator;

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

	@RequestMapping(path="/resources", method=RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public String getNumberOfResources(@RequestParam(name="Name", required=false) List<String> resourceNames, @RequestParam(name="Direction", required=false) String direction) {
		Set<String> resources = nominationRepository.findAll().stream().map(n -> n.getResource())
				.filter(r -> resourceNames == null || resourceNames.contains(r)).collect(Collectors.toSet());
		return "Found the following resources: " + (resources != null ? String.join(", ", resources) : "N/A");
	}

	@CrossOrigin
	@RequestMapping(path="/nominations", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResourcesDTO getNominations(@RequestParam(name="page") Long page, @RequestParam("limit") Long limit
			,@RequestParam(name="region", required=false) String region, @RequestParam(name="resource", required=false) String resource) {
		Pageable pageable = new PageRequest(page.intValue() - 1, limit.intValue());
		Page<ResourceNomination> nominations;
		if(!StringUtils.isEmpty(region) && !StringUtils.isEmpty(resource)) {
			nominations = nominationRepository.findByRegionContainingIgnoreCaseAndResourceContainingIgnoreCaseOrderByResource(region, resource, pageable);

		} else if(!StringUtils.isEmpty(region)) {
			nominations = nominationRepository.findByRegionContainingIgnoreCaseOrderByResource(region, pageable);
		} else if(!StringUtils.isEmpty(resource)) {
			nominations = nominationRepository.findByResourceContainingIgnoreCaseOrderByResource(resource, pageable);
		} else {
			nominations = nominationRepository.findAll(pageable);
		}
		return transformer.transform(nominations, pageable);
	}

	@CrossOrigin
	@RequestMapping(path="/nominations/{nominationId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getNomination(@PathVariable("nominationId") Long nominationId) {
		ResourceNomination nomination = nominationRepository.findOne(nominationId);
		if(nomination == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(transformer.transform(nomination));
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
					|| nominationDto.getRegion() == null
					|| nominationDto.getQuantity() == null
					|| nominationDto.getUnit() == null
					|| nominationDto.getDirection() == null) {
				throw new IllegalArgumentException("Either resource, region, quantity, unit or direction was null in nomination!");
			}
		}
		ResourceNominationAssociation association = new ResourceNominationAssociation();
		association.setParticipant(request.getParticipant());
		association.setNominations(request.getNominations().stream().map(n -> transformer.transform(n)).collect(Collectors.toList()));
		final ResourceNominationAssociation managedAssociation = nominationAssociationRepository.save(association);
		List<Long> ids = association.getNominations().stream().map(n -> {n.setAssociation(managedAssociation); n.setMatchedNominations(new ArrayList<>()); return n;}).map(n -> nominationRepository.save(n).getId()).collect(Collectors.toList());
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
	
	@RequestMapping(value="/reports", method=RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamSource> getReport(@RequestParam(name="type", required=false) String type) throws IOException, DRException {
		Report report = ReportGenerator.createReport(type, nominationRepository.findAll());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("content-disposition", "attachment; filename=ResourceReport_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH:mm:ss")) + ".pdf");
		responseHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
		return ResponseEntity.ok().contentLength(report.getContentLength()).contentType(
				MediaType.APPLICATION_OCTET_STREAM)
				.headers(responseHeaders)
				.body(new InputStreamResource(report.getReportStream()));
	}
}
