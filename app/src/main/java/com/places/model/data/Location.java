package com.places.model.data;

import com.google.gson.annotations.SerializedName;



/**
 * A location of place.
 *
 * @author Gilad Opher
 */
public class Location{


	@SerializedName("lat")
	public double lat;


	@SerializedName("lng")
	public double lng;


	public Location(double lat, double lng){
		this.lat = lat;
		this.lng = lng;
	}
}
