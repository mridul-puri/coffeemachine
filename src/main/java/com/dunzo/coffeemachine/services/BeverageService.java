package com.dunzo.coffeemachine.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.dunzo.coffeemachine.dao.BeveragesDAO;
import com.dunzo.coffeemachine.dao.InventoryDAO;
import com.dunzo.coffeemachine.dao.MachineSpecsDAO;
import com.dunzo.coffeemachine.models.Beverages;
import com.dunzo.coffeemachine.models.Ingredients;

public class BeverageService implements Runnable {

	public String bvg;
	
	public BeverageService(String beverage)
	{
		this.bvg = beverage;
	}
	
	public void run() {
		Object resp = pourBeverage(bvg);
		if(resp == null)
			System.out.println("Beverage does not exist.");
		else
		{
			if((Boolean) resp)
				MachineSpecsDAO.beverages_poured += 1;
			else
				System.out.println("Beverage not available.. Please refill.");
		}
    }
	
	public Object pourBeverage(String bvg)
	{
		//Checks if the input beverage exists and if all its ingredients are available
		//Updates the inventory if the beverage is eligible to serve
		
		if(checkIfValid(bvg))
		{
			HashMap<String, Integer> recipe = getRecipe(bvg);
			if(checkIfAvailable(recipe))
			{
				updateInventory(new ArrayList<String>(recipe.keySet()));
				return true;
			}
		}
		else
			return null;
		return false;
	}
	
	public Boolean checkIfValid(String bvg)
	{
		//Checks if the input beverage exists in the coffee machine
		
		ArrayList<Beverages> bvgList = BeveragesDAO.list_of_beverages;
		Iterator<Beverages> itr = bvgList.iterator();
		while(itr.hasNext())
		{
			Beverages bev = itr.next();
			if(bvg.equals(bev.getName()))
				return true;
		}
		return false;
	}
	
	public HashMap<String, Integer> getRecipe(String bvg)
	{
		//Fetches the required ingredients and their quantity for the input beverage
		
		HashMap<String, Integer> recipe = new HashMap<String, Integer>();
		ArrayList<Beverages> bvgList = BeveragesDAO.list_of_beverages;
		Iterator<Beverages> itr = bvgList.iterator();
		while(itr.hasNext())
		{
			Beverages beverage = itr.next();
			if(bvg.equals(beverage.getName()))
				recipe = beverage.getRecipe();
		}
		return recipe;
	}
	
	public Boolean checkIfAvailable(HashMap<String, Integer> recipe)
	{
		//Checks if the coffee machine has the required ingredients (in required quantity) of the input beverage
		
		Boolean resp = true;
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(recipe.containsKey(ingr.getName()))
			{
				if(ingr.getQuantity() > recipe.get(ingr.getName()))
					resp = resp && true;
				else
					resp = resp && false;
			}
		}
		return resp;
	}
	
	public void updateInventory(ArrayList<String> list_of_ingredients)
	{
		//Updates the inventory after serving a beverage
		
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		for(int i = 0;i < ingrList.size();i++)
		{
			for(int j = 0;j < list_of_ingredients.size();j++)
			{
				Ingredients ingr1 = ingrList.get(i);
				String ingr2 = list_of_ingredients.get(j);
				if(ingr1.getName().equals(ingr2))
				{
					ingr1.setQuantity(ingr1.getQuantity() - InventoryDAO.getIngredientByName(ingr2).getQuantity());
				}
			}
		}
	}	
	
}
