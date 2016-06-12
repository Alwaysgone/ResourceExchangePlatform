package at.ac.tuwien.rep.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.rep.dao.ResourceRegionRepository;
import at.ac.tuwien.rep.model.ResourceAllocation;
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
	
	public ResourcesDTO transform(List<ResourceAllocation> allocations, List<ResourceNomination> nominations) {
		ResourcesDTO dto = new ResourcesDTO();
		dto.setAllocations(allocations.stream().map(a -> transform(a)).collect(Collectors.toList()));
		dto.setNominations(nominations.stream().map(n -> transform(n)).collect(Collectors.toList()));
		return dto;
	}

	public ResourceNominationDTO transform(ResourceNomination nomination) {
		ResourceNominationDTO dto = new ResourceNominationDTO();
		dto.setQuantity(nomination.getQuantity());
		dto.setDirection(nomination.getDirection().name());
		dto.setId(nomination.getId());
		dto.setResource(nomination.getResource());
		dto.setUnit(nomination.getUnit());
		dto.setRegion(nomination.getRegion().getName());
//		dto.setMatchedNominations(nomination.getMatchedNominations().stream().map(n -> transform(n)).collect(Collectors.toList()));
		return dto;
	}

	public ResourceAllocationDTO transform(ResourceAllocation allocation) {
		ResourceAllocationDTO dto = new ResourceAllocationDTO();
		dto.setId(allocation.getId());
		dto.setNomination(transform(allocation.getNomination()));
		dto.setMatchedNominations(allocation.getMatchedNominations().stream().map(n -> transform(n)).collect(Collectors.toList()));
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
