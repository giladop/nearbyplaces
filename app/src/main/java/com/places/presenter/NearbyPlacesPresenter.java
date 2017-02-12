package com.places.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import com.places.model.repository.NearbyPlacesRepository;

import com.places.model.data.Addresses;
import com.places.model.data.Place;
import com.places.model.data.Places;

import java.util.*;



/**
 * The nearby places preesenter is the connected link between data and view.
 *
 * @author Gilad Opher
 */
public class NearbyPlacesPresenter implements NearbyPlacesPresenterViewContract.UserActionsListener
	,PresenterRepositoryContract.GetAddressCallback, PresenterRepositoryContract.GetPlacesCallback{


	/**
	 * The view.
	 */
	private NearbyPlacesPresenterViewContract.View theView;


	/**
	 * The data
	 */
	private PresenterRepositoryContract repository;


	@Inject
	public NearbyPlacesPresenter(PresenterRepositoryContract repository){
		this.repository = repository;
	}


	/* for testing purpose */
	public NearbyPlacesPresenter(PresenterRepositoryContract repository, NearbyPlacesPresenterViewContract.View theView){
		this.repository = repository;
		this.theView = theView;
	}


	/**
	 * Get {@link Addresses} from {@link NearbyPlacesRepository} if return true invoke show loading indicator.
	 */
	@Override
	public void getAddress(@NonNull LatLng location){
		if (repository.getAddress(location, this)){
			if (theView != null)
				theView.showAddressProgressIndicator();
		}
	}


	/**
	 * Get {@link Places} from {@link NearbyPlacesRepository} if return true invoke show loading indicator.
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
	public void bind(NearbyPlacesPresenterViewContract.View view){
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
	 * if data is valid and view is available, return success result to view. otherwise return places were not found.
	 */
	@Override
	public void onPlacesLoaded(Places places, Places oldPlaces){
		if (theView == null) return;

		theView.hidePlacesProgressIndicator();
		if (places == null || places.placeList == null || places.placeList.isEmpty())
			theView.onPlacesNotAvailable();
		else{

			theView.onNearByPlacesLoaded(places.placeList);

			List<Place> placesToAdd = findNewPlaces(places, oldPlaces);
			List<Place> placesToRemove = findMissingPlaces(places, oldPlaces);
			theView.onNearByMapPlacesLoaded(placesToAdd, placesToRemove);
		}
	}



	/**
	 * For map presenting purpose find only new places that needed to add to map.
	 */
	private List<Place> findNewPlaces(@NonNull Places places, @Nullable Places oldPlaces){
		if (oldPlaces == null) return places.placeList;

		return findDistinctPlaces(places.placeList, oldPlaces.placeList);
	}



	/**
	 * For map presenting purpose find only places that needed to be removed from map.
	 */
	private List<Place> findMissingPlaces(@NonNull Places places, @Nullable Places oldPlaces){
		if (oldPlaces == null) return null;

		return findDistinctPlaces(oldPlaces.placeList, places.placeList);
	}



	/**
	 * Return distinct elements between two lists.
	 */
	private List<Place> findDistinctPlaces(@NonNull List<Place> from, @NonNull List<Place> to){

		Set<Place> toSet = new HashSet<>();
		for(Place toPlace : to)
			toSet.add(toPlace);

		List<Place> distinct = new ArrayList<>();
		for(Place place : from){
			if (!toSet.contains(place))
				distinct.add(place);
		}

		return distinct;
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
