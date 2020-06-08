package com.dunzo.coffeemachine.dao;

import java.util.concurrent.ExecutorService;

import org.springframework.stereotype.Repository;

@Repository
public class MachineSpecsDAO {
	public static volatile int no_of_outlets;
	public static volatile ExecutorService outlets_executor;
	public static volatile int beverages_poured = 0;
}
