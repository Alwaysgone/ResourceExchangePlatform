package at.ac.tuwien.rep.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class ResourceRequest {
	@Id
	private String id;
	private List<ResourceNomination> nominations;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public List<ResourceNomination> getNominations() {
		return nominations;
	}
	
	public void setNominations(List<ResourceNomination> nominations) {
		this.nominations = nominations;
	}
}
