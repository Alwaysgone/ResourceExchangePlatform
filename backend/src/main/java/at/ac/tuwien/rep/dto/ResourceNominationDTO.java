package at.ac.tuwien.rep.dto;

import java.math.BigDecimal;
import java.util.List;

public class ResourceNominationDTO {
	private Long id;
	private String resource;
	private BigDecimal quantity;
	private String unit;
	private String direction;
	private String region;
	private List<Long> matchedNominations;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public List<Long> getMatchedNominations() {
		return matchedNominations;
	}

	public void setMatchedNominations(List<Long> matchedNominations) {
		this.matchedNominations = matchedNominations;
	}
}
