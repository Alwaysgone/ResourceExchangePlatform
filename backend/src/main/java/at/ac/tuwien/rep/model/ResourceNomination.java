package at.ac.tuwien.rep.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="resource_nomination")
public class ResourceNomination {
	@Id
	@GeneratedValue
	private Long id;
	private String resource;
	private BigDecimal quantity;
	private String unit;
	private ResourceDirection direction;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="region_id", nullable=false)
	private ResourceRegion region;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="matched_nominations", joinColumns={@JoinColumn(name="offer_id", referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="demand_id", referencedColumnName="id")})
	private List<ResourceNomination> matchedNominations;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="association_id", nullable=false)
	private ResourceNominationAssociation association;
	
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public ResourceDirection getDirection() {
		return direction;
	}

	public void setDirection(ResourceDirection direction) {
		this.direction = direction;
	}

	public ResourceRegion getRegion() {
		return region;
	}

	public void setRegion(ResourceRegion region) {
		this.region = region;
	}

	public ResourceNominationAssociation getAssociation() {
		return association;
	}

	public void setAssociation(ResourceNominationAssociation association) {
		this.association = association;
	}

	public List<ResourceNomination> getMatchedNominations() {
		return matchedNominations;
	}

	public void setMatchedNominations(List<ResourceNomination> matchedNominations) {
		this.matchedNominations = matchedNominations;
	}
}
