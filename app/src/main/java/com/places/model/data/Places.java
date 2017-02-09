package com.places.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;



/**
 * The result object that return from the nearby search API.
 *
 * @author Gilad Opher
 */
public class Places{


	@SerializedName("results")
	public List<Place> placeList;


	public Places(List<Place> placeList){
		this.placeList = placeList;
	}

}
