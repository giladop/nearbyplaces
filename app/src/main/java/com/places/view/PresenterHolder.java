package com.places.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.places.presenter.NearbyPlacesPresenter;


/**
 * A presenter holder for keeping during configuration change.
 *
 * @author Gilad Opher
 */
public class PresenterHolder extends Fragment{



	/**
	 * The presenter.
	 */
	private NearbyPlacesPresenter nearbyPlacesPresenter;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		//keep presenter alive during configuration change.
		setRetainInstance(true);
	}


	/**
	 * save the presenter before {@link Activity} got destroyed.
	 */
	public void setNearbyPlacesPresenter(NearbyPlacesPresenter nearbyPlacesPresenter){
		this.nearbyPlacesPresenter = nearbyPlacesPresenter;
	}


	/**
	 * Return the {@link NearbyPlacesPresenter}. Note this might be null
	 * when app back from background and activity got destroyed (i.e. run with "Don't keep Activities")
	 */
	public NearbyPlacesPresenter getNearbyPlacesPresenter(){
		return nearbyPlacesPresenter;
	}


}
