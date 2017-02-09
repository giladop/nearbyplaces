package com.places.model;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;

import com.places.model.data.*;
import com.places.model.remote.RemotePlacesDataSource;
import com.places.presenter.NearbyPlacesPresenter;



/**
 * Places and address data repository. Responsible for getting the data
 * (in this impl, only from network, but it can be extended easily to get data from other sources)
 * Also, save last {@link Addresses} and {@link Places} for reuse (i.e. orientation change).
 *
 * @author Gilad Opher
 */
public class PlacesRepository implements PlacesDataSource{



	/**
	 * The data source that we'll use for getting data from.
	 */
	private PlacesDataSource placesDataSource;


	/**
	 * A {@link Addresses} cache.
	 */
	private Pair<LatLng, Addresses> addressCache;


	/**
	 * A {@link Places} cache.
	 */
	private Pair<LatLng, Places> placesCache;


	@Inject
	public PlacesRepository(RemotePlacesDataSource placesDataSource){
		this.placesDataSource = placesDataSource;
	}


	/**
	 * Get {@link Addresses} by {@link LatLng}.
	 * In case {@link Addresses} is in cache, return false to presenter, (indication for not showing loading)
	 * Result return (anyway) through {@link GetAddressCallback} callback.
	 */
	@Override
	public boolean getAddress(@NonNull final LatLng location, @NonNull final GetAddressCallback callback){
		if (addressCache != null && addressCache.first != null && addressCache.first.equals(location)){
			callback.onAddressLoaded(addressCache.second);
			return false;
		}

		placesDataSource.getAddress(location, new GetAddressCallback(){
			@Override
			public void onAddressLoaded(Addresses address){
				if (validAddress(address))
					addressCache = Pair.create(location, address);
				callback.onAddressLoaded(address);
			}

			@Override
			public void onAddressNotAvailable(){
				callback.onAddressNotAvailable();
			}
		});

		return true;
	}


	/**
	 * Get {@link Places} by {@link LatLng}.
	 * In case {@link Places} in cache return false to presenter (indication for not showing loading)
	 * Result return (anyway) through {@link GetAddressCallback} callback.
	 */
	@Override
	public boolean getPlaces(@NonNull final LatLng location, @NonNull final GetPlacesCallback callback){

		if (placesCache != null && placesCache.first != null && placesCache.first.equals(location)){
			callback.onPlacesLoaded(placesCache.second);
			return false;
		}

		placesDataSource.getPlaces(location, new GetPlacesCallback(){
			@Override
			public void onPlacesLoaded(Places places){
				if (validPlaces(places))
					placesCache = Pair.create(location, places);

				callback.onPlacesLoaded(places);
			}

			@Override
			public void onPlacesNotAvailable(){
				callback.onPlacesNotAvailable();
			}
		});
		return true;
	}


	/**
	 * Check {@link Addresses} validation
	 */
	boolean validAddress(Addresses address){
		return address != null && address.addressList != null && !address.addressList.isEmpty();
	}


	/**
	 * Check {@link Places} validation
	 */
	private boolean validPlaces(Places places){
		return places != null && places.placeList != null;
	}


}
