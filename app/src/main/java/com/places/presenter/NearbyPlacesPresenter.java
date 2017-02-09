package com.places.presenter;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import com.places.model.PlacesRepository;
import com.places.presenter.NearbyPlacesPresenterContract;

import com.places.model.data.Addresses;
import com.places.model.PlacesDataSource;
import com.places.model.data.Places;



/**
 * The nearby places preesenter is the connected link between data and view.
 *
 * @author Gilad Opher
 */
public class NearbyPlacesPresenter implements NearbyPlacesPresenterContract.UserActionsListener
	,PlacesDataSource.GetAddressCallback, PlacesDataSource.GetPlacesCallback{


	/**
	 * The view.
	 */
	private NearbyPlacesPresenterContract.View theView;


	/**
	 * The data
	 */
	private PlacesDataSource repository;


	@Inject
	public NearbyPlacesPresenter(PlacesDataSource repository){
		this.repository = repository;
	}


	/* for testing purpose */
	public NearbyPlacesPresenter(PlacesDataSource repository, NearbyPlacesPresenterContract.View theView){
		this.repository = repository;
		this.theView = theView;
	}


	/**
	 * Get {@link Addresses} from {@link PlacesRepository} if return true invoke show loading indicator.
	 */
	@Override
	public void getAddress(@NonNull LatLng location){
		if (repository.getAddress(location, this)){
			if (theView != null)
				theView.showAddressProgressIndicator();
		}
	}


	/**
	 * Get {@link Places} from {@link PlacesRepository} if return true invoke show loading indicator.
	 */
	@Override
	public void getNearByPlaces(@NonNull LatLng location){

		if (repository.getPlaces(location, this)){
			if (theView != null)
				theView.showPlacesProgressIndicator();
		}
	}


	/**
	 * connect view and presenter.
	 */
	@Override
	public void bind(NearbyPlacesPresenterContract.View view){
		this.theView = view;
	}


	/**
	 * disconnect view and presenter.
	 */
	@Override
	public void unbind(){
		this.theView = null;
	}


	/**
	 * if data is valid, return success result to view. otherwise return location was not found.
	 */
	@Override
	public void onAddressLoaded(Addresses addresses){
		if (theView == null) return;

		theView.hideAddressProgressIndicator();
		if (validateAddress(addresses))
			theView.onAddressNotAvailable();
		else{

			//pick the first item in list assuming it's the most relevant.
			String name = addresses.addressList.get(0).name;
			theView.onAddressFound(name);
		}
	}


	public  boolean validateAddress(Addresses addresses){
		return addresses == null || addresses.addressList == null || addresses.addressList.isEmpty();
	}


	/**
	 * Return location was not found.
	 */
	@Override
	public void onAddressNotAvailable(){
		if (theView != null){
			theView.hideAddressProgressIndicator();
			theView.onAddressNotAvailable();
		}
	}


	/**
	 * if data is valid, return success result to view. otherwise return places were not found.
	 */
	@Override
	public void onPlacesLoaded(Places places){
		if (theView == null) return;

		theView.hidePlacesProgressIndicator();
		if (places == null || places.placeList == null || places.placeList.isEmpty())
			theView.onPlacesNotAvailable();
		else
			theView.onNearByPlacesLoaded(places.placeList);
	}


	/**
	 * Return places were not found.
	 */
	@Override
	public void onPlacesNotAvailable(){
		if (theView != null){
			theView.onPlacesNotAvailable();
			theView.showPlacesProgressIndicator();
		}
	}


}
