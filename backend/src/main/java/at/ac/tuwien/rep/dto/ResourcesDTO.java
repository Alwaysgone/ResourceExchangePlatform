package at.ac.tuwien.rep.dto;

import java.util.List;

public class ResourcesDTO {
	private List<ResourceAllocationDTO> allocations;
	private List<ResourceNominationDTO> nominations;
	
	public List<ResourceAllocationDTO> getAllocations() {
		return allocations;
	}

	public void setAllocations(List<ResourceAllocationDTO> allocations) {
		this.allocations = allocations;
	}
	
	public List<ResourceNominationDTO> getNominations() {
		return nominations;
	}
	
	public void setNominations(List<ResourceNominationDTO> nominations) {
		this.nominations = nominations;
	}
}
