package at.ac.tuwien.rep.dto;

import java.util.List;

public class ResourceRequestDTO {
	private String participant;
	private List<ResourceNominationDTO> nominations;
	
	public String getParticipant() {
		return participant;
	}
	
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	
	public List<ResourceNominationDTO> getNominations() {
		return nominations;
	}
	
	public void setNominations(List<ResourceNominationDTO> nominations) {
		this.nominations = nominations;
	}
}
