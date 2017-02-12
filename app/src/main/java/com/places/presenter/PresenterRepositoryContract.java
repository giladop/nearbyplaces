package com.places.presenter;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import com.places.model.data.Addresses;
import com.places.model.data.Places;
import com.places.model.repository.NearbyPlacesRepository;



/**
 * The contract between {@link NearbyPlacesPresenter} and {@link NearbyPlacesRepository}.
 *
 * @author Gilad Opher
 */
public interface PresenterRepositoryContract{



	/**
	 * Call back from data repository layer
	 */
	interface GetPlacesCallback {


		void onPlacesLoaded(Places places, Places oldPlaces);


		void onPlacesNotAvailable();
	}



	/**
	 * Call back from data repository
	 */
	interface GetAddressCallback {


		void onAddressLoaded(Addresses address);


		void onAddressNotAvailable();
	}


	/**
	 * Get address by {@link LatLng} location. return boolean indication if long operation is have to be done.
	 * actual result, return using {@link GetAddressCallback}.
	 */
	boolean getAddress(@NonNull LatLng location, @NonNull GetAddressCallback callback);


	/**
	 * Get places by {@link LatLng} location. return boolean indication if long operation is have to be done.
	 * actual result, return using {@link GetPlacesCallback}.
	 */
	boolean getPlaces(@NonNull LatLng location, @NonNull GetPlacesCallback callback);


}
