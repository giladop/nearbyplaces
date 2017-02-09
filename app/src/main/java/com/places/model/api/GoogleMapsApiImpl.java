package com.places.model.api;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.places.model.PlacesDataSource;
import com.places.model.api.GoogleMapsApi;
import com.places.model.data.Addresses;
import com.places.model.data.Places;


/**
 * Naive implementation of restApi using {@link Retrofit}.
 *
 * @author Gilad Opher
 */
public class GoogleMapsApiImpl{



	/**
	 * The API declaration interface.
	 */
	private GoogleMapsApi api;


	@Inject
	public GoogleMapsApiImpl(GoogleMapsApi api){
		this.api = api;
	}


	/**
	 * Reverse geocode
	 */
	public void getAddress(@NonNull LatLng location, @NonNull final PlacesDataSource.GetAddressCallback callback){
		String locationParam = location.latitude + "," + location.longitude;
		api.getAddress(locationParam).enqueue(new Callback<Addresses>(){
			@Override
			public void onResponse(Response<Addresses> response){
				callback.onAddressLoaded(response.body());
			}

			@Override
			public void onFailure(Throwable t){
				callback.onAddressNotAvailable();
			}
		});
	}


	/**
	 * Nearby places.
	 */
	public void getPlaces(@NonNull LatLng location, @NonNull final PlacesDataSource.GetPlacesCallback callback){
		String locationParam = location.latitude + "," + location.longitude;
		api.getPlaces(locationParam).enqueue(new Callback<Places>(){
			@Override
			public void onResponse(Response<Places> response){
				callback.onPlacesLoaded(response.body());
			}

			@Override
			public void onFailure(Throwable t){
				callback.onPlacesNotAvailable();
			}
		});
	}

}
