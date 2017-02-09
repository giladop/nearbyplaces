package com.places.presenter;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import com.places.model.data.Place;



/**
 * The contract between {@link NearbyPlacesPresenter} and it's {@link View}
 *
 * @author Gilad Opher
 */
public interface NearbyPlacesPresenterContract{


	/**
	 * The methods the {@link View} expose for {@link NearbyPlacesPresenter}.
	 */
	interface View{


		void showPlacesProgressIndicator();


		void hidePlacesProgressIndicator();


		void showAddressProgressIndicator();


		void hideAddressProgressIndicator();


		void onNearByPlacesLoaded(List<Place> places);


		void onAddressFound(String Address);


		void onPlacesNotAvailable();


		void onAddressNotAvailable();

	}


	/**
	 * The methods the {@link NearbyPlacesPresenter} expose for {@link View}.
	 */
	interface UserActionsListener{


		void getAddress(LatLng location);


		void getNearByPlaces(LatLng location);


		void bind(View v);


		void unbind();
	}


}
