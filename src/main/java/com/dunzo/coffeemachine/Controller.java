package com.dunzo.coffeemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dunzo.coffeemachine.dao.BeveragesDAO;
import com.dunzo.coffeemachine.dao.InventoryDAO;
import com.dunzo.coffeemachine.dao.MachineSpecsDAO;
import com.dunzo.coffeemachine.models.MachineConfig;


@RestController
public class Controller 
{
	@Autowired
	MachineConfig mconf;
	MachineSpecsDAO specsdDao;
	InventoryDAO inventoryDao;
	BeveragesDAO beveragesDao;
	
	public Controller()
	{
		
	}
		
}
