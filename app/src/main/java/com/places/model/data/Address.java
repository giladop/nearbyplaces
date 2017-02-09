package com.places.model.data;

import com.google.gson.annotations.SerializedName;



/**
 * Single address object.
 *
 * @author Gilad Opher
 */
public class Address{


	@SerializedName("formatted_address")
	public String name;


	public Address(String name){
		this.name = name;
	}
}
