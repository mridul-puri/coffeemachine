package com.dunzo.coffeemachine.dao;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.stereotype.Repository;

import com.dunzo.coffeemachine.models.Ingredients;

@Repository
public class InventoryDAO {

	public static volatile ArrayList<Ingredients> list_of_ingredients = new ArrayList<Ingredients>();
	
	public static Ingredients getIngredientByName(String name)
	{
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(ingr.getName().equals(name))
				return ingr;
		}
		return null;
	}
}
