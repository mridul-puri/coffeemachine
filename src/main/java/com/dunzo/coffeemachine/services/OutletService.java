package com.dunzo.coffeemachine.services;

import com.dunzo.coffeemachine.dao.MachineSpecsDAO;

public class OutletService {

	public static void selectOutlet(String bvg)
	{
		//Select a thread and execute for the input beverage
		
		Runnable outlet = new BeverageService(bvg);
        MachineSpecsDAO.outlets_executor.execute(outlet);
        MachineSpecsDAO.outlets_executor.shutdown();
        while (!MachineSpecsDAO.outlets_executor.isTerminated()) {
        }
	}
}
