package com.places.model.repository;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.places.model.data.Addresses;
import com.places.model.data.Places;



/**
 * The contract between {@link NearbyPlacesRepository} and remote API layer.
 *
 * @author Gilad Opher
 */
public interface RemoteRepository{


	/**
	 * Call back from API layer
	 */
	interface GetPlacesCallback {


		void onPlacesLoaded(Places newPlaces);


		void onPlacesNotAvailable();
	}


	/**
	 * Call back from API layer
	 */
	interface GetAddressCallback {


		void onAddressLoaded(Addresses address);


		void onAddressNotAvailable();
	}


	/**
	 * Get address by {@link LatLng} location. return boolean indication if long operation is have to be done.
	 * actual result, return using {@link RemoteRepository.GetAddressCallback}.
	 */
	boolean getAddress(@NonNull LatLng location, @NonNull GetAddressCallback callback);


	/**
	 * Get places by {@link LatLng} location. return boolean indication if long operation is have to be done.
	 * actual result, return using {@link RemoteRepository.GetPlacesCallback}.
	 */
	boolean getPlaces(@NonNull LatLng location, @NonNull GetPlacesCallback callback);
}
