package com.places.model.repository;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.places.model.api.GoogleMapsApiImpl;
import com.places.model.data.Addresses;
import com.places.model.data.Places;

import javax.inject.Inject;


/**
 * Remote data source. fetch data from google maps API's
 *
 * @author Gilad Opher
 */
public class RemotePlacesRepository implements RemoteRepository{


	/**
	 * The API retrofit impl.
	 */
	private GoogleMapsApiImpl api;


	@Inject
	public RemotePlacesRepository(GoogleMapsApiImpl api){
		this.api = api;
	}


	@Override
	public boolean getAddress(@NonNull LatLng location, @NonNull final GetAddressCallback callback){
		api.getAddress(location, new RemoteRepository.GetAddressCallback(){
			@Override
			public void onAddressLoaded(Addresses address){

				callback.onAddressLoaded(address);
			}

			@Override
			public void onAddressNotAvailable(){
				callback.onAddressNotAvailable();
			}
		});
		return true;
	}


	@Override
	public boolean getPlaces(@NonNull LatLng location, @NonNull final RemoteRepository.GetPlacesCallback callback){
		api.getPlaces(location, new RemoteRepository.GetPlacesCallback(){

			@Override
			public void onPlacesLoaded(Places newPlaces){
				callback.onPlacesLoaded(newPlaces);
			}

			@Override
			public void onPlacesNotAvailable(){
				callback.onPlacesNotAvailable();
			}
		});
		return true;
	}
}
