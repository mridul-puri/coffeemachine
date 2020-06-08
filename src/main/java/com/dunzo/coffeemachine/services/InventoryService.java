package com.dunzo.coffeemachine.services;

import java.util.ArrayList;
import java.util.Iterator;

import com.dunzo.coffeemachine.dao.InventoryDAO;
import com.dunzo.coffeemachine.models.Ingredients;

public class InventoryService {

	public ArrayList<Ingredients> checkAllRunningLow()
	{
		//Checks and reports all the ingredients that are below threshold
		
		ArrayList<Ingredients> resp = new ArrayList<Ingredients>();
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(ingr.getRunning_low())
				resp.add(ingr);
		}
		return resp;
	}
	
	public Boolean checkRunningLow(String name)
	{
		//Checks if the input ingredient is below threshold
		
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(ingr.getName().equals(name))
			{
				if(ingr.getRunning_low())
					return true;
			}
		}
		return false;
	}

	public ArrayList<Boolean> checkRunningLow(ArrayList<String> list_of_ingredients)
	{
		//Checks if the input ingredients are below threshold
		
		ArrayList<Boolean> resp = new ArrayList<Boolean>();
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(list_of_ingredients.contains(ingr.getName()))
			{
				if(ingr.getRunning_low())
					resp.add(true);
				else
					resp.add(false);
			}
		}
		return resp;
	}
	
	public void refillAll()
	{
		//Refills all the ingredients upto their full capacity
		
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(ingr.getQuantity() < ingr.getCapacity())
				ingr.setQuantity(ingr.getCapacity());
		}
	}
	
	public void refillRunningLow()
	{
		//Refills the ingredients that are below threshold upto their full capacity
		
		ArrayList<Ingredients> ingrRunningLow = checkAllRunningLow();
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(ingrRunningLow.contains(ingr))
				ingr.setQuantity(ingr.getCapacity());
		}
	}
	
	public void refillIngredient(String name)
	{
		//Refills the input ingredient upto its full capacity
		
		if(checkRunningLow(name)) {
			Ingredients ingr = InventoryDAO.getIngredientByName(name);
			ingr.setQuantity(ingr.getCapacity());
		}	
	}
	
	public void refillIngredients(ArrayList<String> list_of_ingredients)
	{
		//Refills the input ingredients upto their full capacity
		
		ArrayList<Boolean> isLow = checkRunningLow(list_of_ingredients);
		for(int i = 0;i < isLow.size();i++)
		{
			if(isLow.get(i))
			{
				Ingredients ingr = InventoryDAO.getIngredientByName(list_of_ingredients.get(i));
				ingr.setQuantity(ingr.getCapacity());
			}
		}
	}	
}
