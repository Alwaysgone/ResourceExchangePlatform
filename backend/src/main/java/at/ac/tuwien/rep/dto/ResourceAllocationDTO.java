package at.ac.tuwien.rep.dto;

import java.util.List;

public class ResourceAllocationDTO {
	private Long id;
	private ResourceNominationDTO nomination;
	private List<ResourceNominationDTO> matchedNominations;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ResourceNominationDTO getNomination() {
		return nomination;
	}

	public void setNomination(ResourceNominationDTO nomination) {
		this.nomination = nomination;
	}

	public List<ResourceNominationDTO> getMatchedNominations() {
		return matchedNominations;
	}

	public void setMatchedNominations(List<ResourceNominationDTO> matchedNominations) {
		this.matchedNominations = matchedNominations;
	}
}
