package com.dunzo.coffeemachine.models;

import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Repository
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredients {
	public String name;
	public String state;
	public String unit;
	public int capacity;
	public int threshold;
	public int quantity;
	public Boolean running_low;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getThreshold() {
		return threshold;
	}
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Boolean getRunning_low() {
		return running_low;
	}
	public void setRunning_low(Boolean running_low) {
		this.running_low = running_low;
	}
	
}
