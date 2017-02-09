package com.places.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;



/**
 * A single place object.
 *
 * @author Gilad Opher
 */
public class Place{


	@SerializedName("place_id")
	public String id;


	@SerializedName("name")
	public String name;


	@SerializedName("icon")
	public String imageUrl;


	@SerializedName("geometry")
	public Geometry geometry;


	@SerializedName("types")
	public List<String> types;


	public Place(String id, String name, String imageUrl, Geometry geometry, List<String> types){
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.geometry = geometry;
		this.types = types;
	}

}
