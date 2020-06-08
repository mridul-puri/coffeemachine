package com.dunzo.coffeemachine.dao;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.dunzo.coffeemachine.models.Beverages;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Repository
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeveragesDAO {

	public static volatile ArrayList<Beverages> list_of_beverages = new ArrayList<Beverages>();

}
