package com.dunzo.coffeemachine.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.dunzo.coffeemachine.dao.BeveragesDAO;
import com.dunzo.coffeemachine.dao.InventoryDAO;
import com.dunzo.coffeemachine.dao.MachineSpecsDAO;
import com.dunzo.coffeemachine.models.Beverages;
import com.dunzo.coffeemachine.models.Ingredients;
import com.dunzo.coffeemachine.models.MachineConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class PowerService {
	
	public void setupMachine()
	{
		//Creates a file coffeeConfig.json and copies data from setup.json to it
		//setup.json : This file is used for fresh setup of the coffee machine
		//coffeeConfg.json : This file is used to load data to coffee machine when started and save in-memory data when stopped
		
		try {
			Path source = Paths.get("setup.json");
			File config = new File("coffeeConfig.json");
			config.createNewFile();
		    Path destination = Paths.get(config.getPath());
			synchronized(this)
			{
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to copy setup configurations.");
		}
	}

	public void startMachine()
	{
		//Reads data from cofeeConfig.json and loads in program memory
		
		JSONParser parser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			Object obj = parser.parse(new FileReader("coffeeConfig.json"));
			JSONObject jsonObject = (JSONObject) obj;
			mapper.registerModule(new Jdk8Module());
			MachineConfig mconf = mapper.readValue(jsonObject.toString(),MachineConfig.class);
			
			//Storing number of outlets
			MachineSpecsDAO.no_of_outlets = mconf.getNo_of_outlets();
			
			//Storing list of ingredients
			
			ArrayList<Ingredients> ingrList = mconf.getList_of_ingredients();
			Iterator<Ingredients> ingrItr = ingrList.iterator();
			while (ingrItr.hasNext()) {
	        	InventoryDAO.list_of_ingredients.add(ingrItr.next());
			}	
			
			//Storing list of beverages
			
			ArrayList<Beverages> bvgList = mconf.getList_of_beverages();
			Iterator<Beverages> bvgItr = bvgList.iterator();
			while (bvgItr.hasNext()) {
	        	BeveragesDAO.list_of_beverages.add(bvgItr.next());
			}				
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//Creating a pool of threads of different outlets
		
		MachineSpecsDAO.outlets_executor = Executors.newFixedThreadPool(MachineSpecsDAO.no_of_outlets);
	}
	
	public void stopMachine()
	{
		//Stores in memory data to coffeeConfig.json to be used when machine restarts
		
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new Jdk8Module());
			FileWriter fileWriter = new FileWriter("coffeeConfig.json",false);
			PrintWriter writer = new PrintWriter(fileWriter);
			MachineConfig mconf = new MachineConfig();
			
			mconf.setNo_of_outlets(MachineSpecsDAO.no_of_outlets);
			mconf.setList_of_ingredients(InventoryDAO.list_of_ingredients);
			mconf.setList_of_beverages(BeveragesDAO.list_of_beverages);
			
			String prettyJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mconf);
	    	writer.println(prettyJsonString);
	    	writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void restartMachine()
	{	
		//stops the coffee machine, loads fresh data from setup.json and restarts again
		//This functionality can be used when new setup is required
		
		stopMachine();
		setupMachine();
		startMachine();
	}
	
}
