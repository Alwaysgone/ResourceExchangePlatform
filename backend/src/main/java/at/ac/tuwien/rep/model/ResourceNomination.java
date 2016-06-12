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
	
//	@ManyToOne
//	@JoinColumn(name="allocation_id", nullable=true)
//	private ResourceAllocation allocation;
	
//	@ManyToMany(cascade = CascadeType.MERGE, mappedBy = "fulfilledMatches", fetch=FetchType.EAGER) 
//	private List<ResourceNomination> fulfilledByMatches;
//	
//	@ManyToMany(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
//	@JoinTable(name = "fulfilling_matches", 
//	            joinColumns = {@JoinColumn(name = "fulfilledByMatches_id")}, 
//	            inverseJoinColumns = {@JoinColumn(name = "fulfilledMatches_id")}) 
//	private List<ResourceNomination> fulfilledMatches;
	
	@ManyToMany(fetch=FetchType.EAGER)
//	@JoinColumn(name="matched_nomination", referencedColumnName="id", unique = false)
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
	
//	public ResourceAllocation getAllocation() {
//		return allocation;
//	}
//
//	public void setAllocation(ResourceAllocation allocation) {
//		this.allocation = allocation;
//	}

	public ResourceNominationAssociation getAssociation() {
		return association;
	}

	public void setAssociation(ResourceNominationAssociation association) {
		this.association = association;
	}

//	public List<ResourceNomination> getFulfilledByMatches() {
//		return fulfilledByMatches;
//	}
//
//	public void setFulfilledByMatches(List<ResourceNomination> fulfilledByMatches) {
//		this.fulfilledByMatches = fulfilledByMatches;
//	}
//
//	public List<ResourceNomination> getFulfilledMatches() {
//		return fulfilledMatches;
//	}
//
//	public void setFulfilledMatches(List<ResourceNomination> fulfilledMatches) {
//		this.fulfilledMatches = fulfilledMatches;
//	}

	public List<ResourceNomination> getMatchedNominations() {
		return matchedNominations;
	}

	public void setMatchedNominations(List<ResourceNomination> matchedNominations) {
		this.matchedNominations = matchedNominations;
	}
	
	public boolean isSatisfied() {
		BigDecimal matchedNominationsQuantity = matchedNominations.stream().map(n -> n.getQuantity()).reduce(BigDecimal.ZERO, (a,b) -> a.add(b));
		System.out.println("Matched quantity " + matchedNominationsQuantity + (matchedNominationsQuantity.compareTo(quantity) >= 0 ? " satisfies " : " does not satisfy ") + quantity + " of demand " + id);
		return matchedNominationsQuantity.compareTo(quantity) >= 0;
	}
}
