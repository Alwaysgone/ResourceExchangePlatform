package at.ac.tuwien.rep.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

public class ResourceNomination {
	@Id
	private String id;
	private String resource;
	private BigDecimal amount;
	private String unit;
	private ResourceDirection direction;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
}
