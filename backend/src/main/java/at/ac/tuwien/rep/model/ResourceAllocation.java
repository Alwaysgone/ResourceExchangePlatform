package at.ac.tuwien.rep.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class ResourceAllocation {
	@Id
	private String id;
	private ResourceNomination nomination;
	private List<ResourceNomination> matchedNominations;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ResourceNomination getNomination() {
		return nomination;
	}

	public void setNomination(ResourceNomination nomination) {
		this.nomination = nomination;
	}

	public List<ResourceNomination> getMatchedNominations() {
		return matchedNominations;
	}

	public void setMatchedNominations(List<ResourceNomination> matchedNominations) {
		this.matchedNominations = matchedNominations;
	}
}
