package com.places.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;



/**
 * The result object that return from the geocode API.
 *
 * @author Gilad Opher
 */
public class Addresses{


	@SerializedName("results")
	public List<Address> addressList;


	public Addresses(List<Address> addressList){
		this.addressList = addressList;
	}

}
