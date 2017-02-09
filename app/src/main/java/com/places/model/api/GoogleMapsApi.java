package com.places.model.api;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.places.model.data.Addresses;
import com.places.model.data.Places;



/**
 *
 * @author Gilad Opher
 */
public interface GoogleMapsApi{


	/**
	 * Reverse geocoding from {@link LatLng} to {@link Address}.
	 */
	@GET("geocode/json?key=AIzaSyBfid5b1VjNv68Xu-lUbMFde7h6Jozq4WU")
	Call<Addresses> getAddress(@Query("latlng") String latlng);


	/**
	 * Nearby places ordered by distance.
	 */
	@GET("place/nearbysearch/json?rankby=distance&key=AIzaSyBu7u0kmoBzVD0ZUipVCx-kF04xluGvSOM")
	Call<Places> getPlaces(@Query("location") String location);


}
