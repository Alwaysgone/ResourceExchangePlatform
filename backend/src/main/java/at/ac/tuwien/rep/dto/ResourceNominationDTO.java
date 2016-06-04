package at.ac.tuwien.rep.dto;

import java.math.BigDecimal;

public class ResourceNominationDTO {
	private String id;
	private ResourceDTO resource;
	private BigDecimal amount;
	private String direction;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public ResourceDTO getResource() {
		return resource;
	}
	
	public void setResource(ResourceDTO resource) {
		this.resource = resource;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
