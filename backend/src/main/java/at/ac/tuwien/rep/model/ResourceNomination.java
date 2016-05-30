package at.ac.tuwien.rep.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

public class ResourceNomination {
	@Id
	private String id;
	private Resource resource;
	private BigDecimal amount;
	private ResourceDirection direction;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public ResourceDirection getDirection() {
		return direction;
	}

	public void setDirection(ResourceDirection direction) {
		this.direction = direction;
	}
}
