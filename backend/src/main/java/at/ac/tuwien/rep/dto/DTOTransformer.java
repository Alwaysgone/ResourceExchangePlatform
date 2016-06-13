package at.ac.tuwien.rep.dto;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import at.ac.tuwien.rep.dao.ResourceRegionRepository;
import at.ac.tuwien.rep.model.ResourceDirection;
import at.ac.tuwien.rep.model.ResourceNomination;
import at.ac.tuwien.rep.model.ResourceRegion;

@Component
public class DTOTransformer {

	private ResourceRegionRepository regionRepository;

	@Autowired
	public DTOTransformer(ResourceRegionRepository regionRepository) {
		this.regionRepository = regionRepository;
	}
	
	public ResourcesDTO transform(Page<ResourceNomination> nominations, Pageable pageable) {
		ResourcesDTO dto = new ResourcesDTO();
		System.out.println("Transforming " + nominations.getContent().size() + " elements");
		Page<ResourceNominationDTO> nominationsPage = new PageImpl<>(nominations.getContent().stream().map(n -> transform(n)).collect(Collectors.toList()), pageable, nominations.getTotalElements());
		System.out.println("Page size is " + pageable.getPageSize() + " number of total elements is " + nominations.getTotalElements());
		dto.setNominations(nominationsPage);
		//		dto.setNominations(nominations.getContent().stream().map(n -> transform(n)).collect(Collectors.toList()));
		System.out.println("Transformed " + dto.getNominations().getContent().size() + " elements");
		return dto;
	}
	
//	public ResourcesDTO transform(List<ResourceNomination> nominations) {
//		ResourcesDTO dto = new ResourcesDTO();
//		dto.setNominations(nominations.stream().map(n -> transform(n)).collect(Collectors.toList()));
//		return dto;
//	}

	public ResourceNominationDTO transform(ResourceNomination nomination) {
		ResourceNominationDTO dto = new ResourceNominationDTO();
		dto.setQuantity(nomination.getQuantity());
		dto.setDirection(nomination.getDirection().name());
		dto.setId(nomination.getId());
		dto.setResource(nomination.getResource());
		dto.setUnit(nomination.getUnit());
		dto.setRegion(nomination.getRegion().getName());
		dto.setMatchedNominations(nomination.getMatchedNominations().stream().map(n -> n.getId()).collect(Collectors.toList()));
		return dto;
	}

	public ResourceNomination transform(ResourceNominationDTO dto) {
		ResourceNomination nomination = new ResourceNomination();
		nomination.setQuantity(dto.getQuantity());
		nomination.setDirection(ResourceDirection.valueOf(dto.getDirection()));
		nomination.setResource(dto.getResource());
		nomination.setUnit(dto.getUnit());
		ResourceRegion region = regionRepository.findByName(dto.getRegion());
		if(region == null) {
			region = new ResourceRegion();
			region.setName(dto.getRegion());
		}
		nomination.setRegion(region);
		return nomination;
	}
}
