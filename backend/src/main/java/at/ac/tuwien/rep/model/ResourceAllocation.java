package at.ac.tuwien.rep.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="resource_allocation")
public class ResourceAllocation {
	@Id
	@GeneratedValue
	private Long id;
	@OneToOne(mappedBy="allocation")
	@JoinColumn(name = "nomination_id", nullable=false)
	private ResourceNomination nomination;
	@OneToMany(mappedBy="allocation", fetch=FetchType.EAGER)
	private List<ResourceNomination> matchedNominations;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
