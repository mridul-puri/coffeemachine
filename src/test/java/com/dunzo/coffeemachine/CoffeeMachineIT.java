package com.dunzo.coffeemachine;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dunzo.coffeemachine.dao.BeveragesDAO;
import com.dunzo.coffeemachine.dao.InventoryDAO;
import com.dunzo.coffeemachine.dao.MachineSpecsDAO;
import com.dunzo.coffeemachine.models.Beverages;
import com.dunzo.coffeemachine.models.Ingredients;
import com.dunzo.coffeemachine.models.MachineConfig;
import com.dunzo.coffeemachine.services.InventoryService;
import com.dunzo.coffeemachine.services.OutletService;
import com.dunzo.coffeemachine.services.PowerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.runners.MethodSorters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoffeeMachineIT {
	@Test
	public void t0_setupMachine()
	{
		PowerService service = new PowerService();
		service.setupMachine();
		Assert.assertTrue(new File("coffeeConfig.json").exists());
	}
	
	@Test
	public void t1_startMachine()
	{
		JSONParser parser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		PowerService service = new PowerService();
		
		try {
			Object obj = parser.parse(new FileReader("coffeeConfig.json"));
			JSONObject jsonObject = (JSONObject) obj;
			mapper.registerModule(new Jdk8Module());
			MachineConfig mconf = mapper.readValue(jsonObject.toString(),MachineConfig.class);
			service.startMachine();
			Assert.assertTrue(mconf.getNo_of_outlets() == MachineSpecsDAO.no_of_outlets);
			Assert.assertTrue(mconf.getList_of_ingredients() == InventoryDAO.list_of_ingredients);
			Assert.assertTrue(mconf.getList_of_beverages() == BeveragesDAO.list_of_beverages);	
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void t2_stopMachine()
	{
		JSONParser parser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		PowerService service = new PowerService();
		service.stopMachine();
		try {
			Object obj = parser.parse(new FileReader("coffeeConfig.json"));
			JSONObject jsonObject = (JSONObject) obj;
			mapper.registerModule(new Jdk8Module());
			MachineConfig mconf = mapper.readValue(jsonObject.toString(),MachineConfig.class);
			Assert.assertTrue(mconf.getNo_of_outlets() == MachineSpecsDAO.no_of_outlets);
			Assert.assertTrue(mconf.getList_of_ingredients() == InventoryDAO.list_of_ingredients);
			Assert.assertTrue(mconf.getList_of_beverages() == BeveragesDAO.list_of_beverages);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void t3_restartMachine()
	{
		
		JSONParser parser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		PowerService service = new PowerService();
		
		int no_of_outlets = MachineSpecsDAO.no_of_outlets;
		ArrayList<Ingredients> list_of_ingredients  = InventoryDAO.list_of_ingredients;
		ArrayList<Beverages> list_of_beverages = BeveragesDAO.list_of_beverages;
		
		service.restartMachine();
		try {
			Object obj = parser.parse(new FileReader("coffeeConfig.json"));
			JSONObject jsonObject = (JSONObject) obj;
			mapper.registerModule(new Jdk8Module());
			MachineConfig mconf = mapper.readValue(jsonObject.toString(),MachineConfig.class);
			Assert.assertTrue(mconf.getNo_of_outlets() == no_of_outlets);
			Assert.assertTrue(mconf.getList_of_ingredients() == list_of_ingredients);
			Assert.assertTrue(mconf.getList_of_beverages() == list_of_beverages);
			}catch (Exception e) {
				e.printStackTrace();
			}	
	}
	
	@Test
	public void t4_pourSingleBeverage()
	{
		OutletService.selectOutlet("Coffee");
		Assert.assertTrue(MachineSpecsDAO.beverages_poured == 1);
		if(MachineSpecsDAO.beverages_poured == 1)
			MachineSpecsDAO.beverages_poured = 0;
	}
	
	@Test
	@Ignore
	public void t5_pourMultipleBeverages()
	{
		ArrayList<String> list_of_beverages = new ArrayList<String>();
		list_of_beverages.add("Coffee");
		list_of_beverages.add("Hot Milk");
		list_of_beverages.add("Green Tea");
		list_of_beverages.add("Hot Water");
		Iterator<String> itr = list_of_beverages.iterator();
		while(itr.hasNext())
		{
			OutletService.selectOutlet(itr.next());
		}
		Assert.assertTrue(MachineSpecsDAO.beverages_poured == 4);
		if(MachineSpecsDAO.beverages_poured == 4)
			MachineSpecsDAO.beverages_poured = 0;
	}
	
	@Test
	public void t6_refillAllIngredients()
	{	
		InventoryService service = new InventoryService();
		service.refillAll();
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			Assert.assertTrue(ingr.getQuantity() != ingr.getCapacity());
		}
	}
	
	@Test
	public void t7_refillIngredientsRunningLow()
	{
		InventoryService service = new InventoryService();
		ArrayList<Ingredients> ingrRunningLow = new ArrayList<Ingredients>();
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(ingr.getRunning_low())
				ingrRunningLow.add(ingr);
		}
		service.refillRunningLow();
		itr = ingrRunningLow.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			Assert.assertTrue(ingr.getQuantity() == ingr.getCapacity());
		}	
	}
	
	@Test
	public void t8_refillSingleIngredient()
	{
		Boolean isLow = false;
		Ingredients currIngr = null;
		InventoryService service = new InventoryService();
		ArrayList<Ingredients> list_of_ingredients = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = list_of_ingredients.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(ingr.getName().equals("Milk"))
			{
				isLow = ingr.getRunning_low();
				currIngr = ingr;
			}
		}
		Assert.assertTrue(isLow);
		service.refillIngredient("Milk");
		Assert.assertTrue(currIngr.getQuantity() == currIngr.getCapacity());
	}

	@Test
	public void t9_refillMultipleIngredients()
	{
		ArrayList<String> list_of_ingredients = new ArrayList<String>();
		list_of_ingredients.add("Milk");
		list_of_ingredients.add("Water");
		list_of_ingredients.add("Steam");
		list_of_ingredients.add("Coffee Beans");
		ArrayList<Boolean> isLow = new ArrayList<Boolean>();
		ArrayList<Ingredients> currIngr = new ArrayList<Ingredients>();
		InventoryService service = new InventoryService();
		ArrayList<Ingredients> ingrList = InventoryDAO.list_of_ingredients;
		Iterator<Ingredients> itr = ingrList.iterator();
		while(itr.hasNext())
		{
			Ingredients ingr = itr.next();
			if(list_of_ingredients.contains(ingr.getName()))
			{
				isLow.add(ingr.getRunning_low());
				currIngr.add(ingr);
			}
		}
		for(int i = 0;i < isLow.size();i++)
		{
			Assert.assertTrue(isLow.get(i));
		}
		service.refillIngredients(list_of_ingredients);
		for(int i = 0;i < currIngr.size();i++)
		{
			Assert.assertTrue(currIngr.get(i).getQuantity() == currIngr.get(i).getCapacity());
		}
	}
}
