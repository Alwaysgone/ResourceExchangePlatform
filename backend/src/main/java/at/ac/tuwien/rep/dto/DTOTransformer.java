package at.ac.tuwien.rep.dto;

import java.util.List;
import java.util.stream.Collectors;

import at.ac.tuwien.rep.model.ResourceAllocation;
import at.ac.tuwien.rep.model.ResourceNomination;

public class DTOTransformer {
	public static ResourcesDTO transform(List<ResourceNomination> nominations, List<ResourceAllocation> allocations) {
		ResourcesDTO dto = new ResourcesDTO();
		dto.setAllocations(allocations.stream().map(a -> transform(a)).collect(Collectors.toList()));
		dto.setNominations(nominations.stream().map(n -> transform(n)).collect(Collectors.toList()));
		return dto;
	}
	
	public static ResourceNominationDTO transform(ResourceNomination nomination) {
		ResourceNominationDTO dto = new ResourceNominationDTO();
		dto.setAmount(nomination.getAmount());
		dto.setDirection(nomination.getDirection().name());
		dto.setId(nomination.getId());
		return dto;
	}
	
	public static ResourceAllocationDTO transform(ResourceAllocation allocation) {
		ResourceAllocationDTO dto = new ResourceAllocationDTO();
		dto.setId(allocation.getId());
		dto.setMatchedNominations(allocation.getMatchedNominations().stream().map(m -> transform(m)).collect(Collectors.toList()));
		dto.setNomination(transform(allocation.getNomination()));
		return dto;
	}
}
