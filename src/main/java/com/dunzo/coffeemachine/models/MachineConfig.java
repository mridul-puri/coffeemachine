package com.dunzo.coffeemachine.models;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public class MachineConfig {
	public int no_of_outlets;
	public ArrayList<Ingredients> list_of_ingredients;
	public ArrayList<Beverages> list_of_beverages;
	
	public int getNo_of_outlets() {
		return no_of_outlets;
	}
	public void setNo_of_outlets(int no_of_outlets) {
		this.no_of_outlets = no_of_outlets;
	}
	public ArrayList<Ingredients> getList_of_ingredients() {
		return list_of_ingredients;
	}
	public void setList_of_ingredients(ArrayList<Ingredients> list_of_ingredients) {
		this.list_of_ingredients = list_of_ingredients;
	}
	public ArrayList<Beverages> getList_of_beverages() {
		return list_of_beverages;
	}
	public void setList_of_beverages(ArrayList<Beverages> list_of_beverages) {
		this.list_of_beverages = list_of_beverages;
	}
	
}
