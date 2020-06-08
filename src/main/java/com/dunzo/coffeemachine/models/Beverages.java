package com.dunzo.coffeemachine.models;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

@Repository
public class Beverages {
	public String name;
	public HashMap<String,Integer> recipe;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, Integer> getRecipe() {
		return recipe;
	}
	public void setRecipe(HashMap<String, Integer> recipe) {
		this.recipe = recipe;
	}
	
}
