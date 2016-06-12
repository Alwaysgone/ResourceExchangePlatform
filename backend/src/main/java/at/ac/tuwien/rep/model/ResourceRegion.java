package at.ac.tuwien.rep.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="resource_region")
public class ResourceRegion {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@OneToMany(mappedBy="region")
	private List<ResourceNomination> nominations;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<ResourceNomination> getNominations() {
		return nominations;
	}

	public void setNominations(List<ResourceNomination> nominations) {
		this.nominations = nominations;
	}
}
