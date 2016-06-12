package at.ac.tuwien.rep.dto;

import java.util.List;

public class ResourcesDTO {
	private List<ResourceNominationDTO> nominations;

	public List<ResourceNominationDTO> getNominations() {
		return nominations;
	}
	
	public void setNominations(List<ResourceNominationDTO> nominations) {
		this.nominations = nominations;
	}
}
