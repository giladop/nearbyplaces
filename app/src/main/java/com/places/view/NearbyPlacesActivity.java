package com.places.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.places.NearbyPlacesApplication;
import com.places.R;
import com.places.di.DaggerPlacesComponent;
import com.places.di.PlacesComponent;
import com.places.di.PlacesModule;
import com.places.model.data.Place;
import com.places.presenter.NearbyPlacesPresenter;
import com.places.presenter.NearbyPlacesPresenterContract;

import java.util.List;

import javax.inject.Inject;



/**
 * Activity that shows nearby places on split view of Map and list. Supporting landscape mode. The Activity Act as "View" in the MVP module
 * and delegate data/action to {@link MapPlacesFragment} and {@link PlacesListFragment}.
 * Dragging the map, refreshing both map and list. If permission granted, the user location will be shown on map.
 */
public class NearbyPlacesActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
		LocationListener, GoogleApiClient.OnConnectionFailedListener, NearbyPlacesPresenterContract.View,
		MapPlacesFragment.MapMoveListener, PlacesListFragment.PlacesListListener{


	/**
	 * Grant permission user location request code.
	 */
	private static final int LOCATION_PERMISSIONS_REQUEST = 5412;


	/**
	 * The {@link GoogleApiClient}.
	 */
	private GoogleApiClient googleApiClient;


	@Inject
	NearbyPlacesPresenter nearbyPlacesPresenter;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		//get the presenter from holder or by inject
		FragmentManager fm = getSupportFragmentManager();
		PresenterHolder presenterHolder = (PresenterHolder) fm.findFragmentByTag("presenterHolder");
		if (presenterHolder == null) {
			fm.beginTransaction().add(new PresenterHolder(), "presenterHolder").commit();
			fm.executePendingTransactions();
			inject();
		}else
			nearbyPlacesPresenter = presenterHolder.getNearbyPlacesPresenter();

		if (nearbyPlacesPresenter == null)
			inject();

		//init google api client
		if (googleApiClient == null){
			googleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addApi(LocationServices.API)
					.build();
		}

		gotoMyLocation();
	}


	/**
	 * Dagger injection.
	 */
	private void inject(){
		PlacesComponent placesComponent =
				DaggerPlacesComponent.builder()
					.apiComponent(NearbyPlacesApplication.getNearbyPlacesApplication(this).getApiComponent())
					.placesModule(new PlacesModule())
					.build();
		placesComponent.inject(this);
	}


	/**
	 * Save reference to {@link NearbyPlacesPresenter}.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState){

		PresenterHolder presenterHolder = (PresenterHolder) getSupportFragmentManager().findFragmentByTag("presenterHolder");
		if (presenterHolder != null)
			presenterHolder.setNearbyPlacesPresenter(nearbyPlacesPresenter);

		super.onSaveInstanceState(outState);
	}


	protected void onStart(){
		super.onStart();
		googleApiClient.connect();
		nearbyPlacesPresenter.bind(this);
	}


	protected void onStop(){
		googleApiClient.disconnect();
		nearbyPlacesPresenter.unbind();
		super.onStop();
	}


	@Override
	public void showPlacesProgressIndicator(){
		getPlacesListFragment().setPlacesProgressBarVisibility(true);
	}


	@Override
	public void hidePlacesProgressIndicator(){
		getPlacesListFragment().setPlacesProgressBarVisibility(false);
	}


	@Override
	public void showAddressProgressIndicator(){
		getPlacesListFragment().setAddressProgressBarVisibility(true);
	}


	@Override
	public void hideAddressProgressIndicator(){
		getPlacesListFragment().setAddressProgressBarVisibility(false);
	}


	@Override
	public void onNearByPlacesLoaded(List<Place> places){
		getPlacesListFragment().setPlaces(places);
		getMapPlacesFragment().updateMapPlaces(places);
	}


	@Override
	public void onAddressFound(String address){
		getPlacesListFragment().setAddressName(address);
	}


	@Override
	public void onPlacesNotAvailable(){
		getPlacesListFragment().onPlacesNotAvailable();
	}


	@Override
	public void onAddressNotAvailable(){
		LatLng currentLocation = getMapPlacesFragment().getCurrentLocation();
		getPlacesListFragment().setAddressName(currentLocation != null ? currentLocation.toString() : getString(R.string.na));
	}


	/**
	 * Ask user's current location.
	 */
	public void gotoMyLocation(){

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

			ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
					LOCATION_PERMISSIONS_REQUEST);

			return;
		}

		getMapPlacesFragment().setMyLocationEnabled(true);

		Location userLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
		LatLng currentLocation = toLatLng(userLocation);
		getMapPlacesFragment().moveMapToLocation(currentLocation);
	}


	/**
	 * convert {@link Location} to {@link LatLng}.
	 */
	private LatLng toLatLng(Location location){
		return (location == null) ? null : new LatLng(location.getLatitude(), location.getLongitude());
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

		if (requestCode == LOCATION_PERMISSIONS_REQUEST){

			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

				getMapPlacesFragment().setMyLocationButtonEnabled(true);
				gotoMyLocation();
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}


	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult){}


	@Override
	public void onConnected(@Nullable Bundle bundle){
		getCurrentLocation();
	}


	@Override
	public void onConnectionSuspended(int i){}


	/**
	 * Get current user's location. Check/Ask for permission before.
	 */
	private void getCurrentLocation(){
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

			getMapPlacesFragment().setMyLocationEnabled(true);
			LocationRequest locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setNumUpdates(1);
			LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
		}
	}


	@Override
	public void onLocationChanged(Location location){
		if (location == null) return;

		getMapPlacesFragment().onLocationChanged(new LatLng(location.getLatitude(), location.getLongitude()));

		if (googleApiClient.isConnected())
			LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
	}


	private PlacesListFragment getPlacesListFragment(){
		return (PlacesListFragment)getSupportFragmentManager().findFragmentById(R.id.places_fragment);
	}


	private MapPlacesFragment getMapPlacesFragment(){
		return (MapPlacesFragment)getSupportFragmentManager().findFragmentById(R.id.map_places_fragment);
	}


	@Override
	public void onMapIdle(LatLng currentLocation){

		nearbyPlacesPresenter.getAddress(currentLocation);
		nearbyPlacesPresenter.getNearByPlaces(currentLocation);
	}


	@Override
	public void onPlaceClicked(Place place){
		getMapPlacesFragment().animateToPlace(place);
	}


}

