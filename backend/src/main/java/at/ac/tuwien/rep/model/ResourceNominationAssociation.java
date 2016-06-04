package at.ac.tuwien.rep.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class ResourceNominationAssociation {
	@Id
	private String id;
	private String participant;
	private List<ResourceNomination> nominations;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}
	
	public List<ResourceNomination> getNominations() {
		return nominations;
	}
	
	public void setNominations(List<ResourceNomination> nominations) {
		this.nominations = nominations;
	}
}
