package at.ac.tuwien.rep.dto;

import org.springframework.data.domain.Page;

public class ResourcesDTO {
	private Page<ResourceNominationDTO> nominations;
	
	public Page<ResourceNominationDTO> getNominations() {
		return nominations;
	}
	
	public void setNominations(Page<ResourceNominationDTO> nominations) {
		this.nominations = nominations;
	}
}
