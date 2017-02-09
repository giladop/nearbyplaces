package com.places.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.places.R;
import com.places.model.data.Place;


/**
 * Map fragment showing current location (if permission granted) and nearby places.
 * dragging the map fetching new places around new location.
 * Also, click on place on Map or on list (from {@link PlacesListFragment}), animate map to place.
 *
 * @author Gilad Opher
 */
public class MapPlacesFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback{


	/**
	 * The google's {@link MapView}
	 */
	@BindView(R.id.mapview)
	MapView mapView;


	/**
	 * The google's {@link GoogleMap} map.
	 */
	GoogleMap map;


	/**
	 * a local store for keep track of which {@link Marker}'s added to map.
	 */
	private Map<String, Marker> placeMarkers = new HashMap<>();


	/**
	 * The current location of Map center.
	 */
	private LatLng currentLocation;


	/**
	 * The last detected location before goes to background.
	 */
	private LatLng savedLocation;


	/**
	 * A callback for notifying on map move events.
	 */
	private MapMoveListener callback;



	/**
	 * Generic place map icon.
	 */
	private BitmapDescriptor genericPlaceIcon;



	private Map<String, MarkerOptions> markerOptionsList = new HashMap<>();



	public MapPlacesFragment(){}


	@Override
	public void onAttach(Context context){
		super.onAttach(context);
		if (context instanceof MapMoveListener)
			callback = (MapMoveListener)context;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.map_places_layout, container, false);
		ButterKnife.bind(this, view);

		if (savedInstanceState != null){
			savedLocation = savedInstanceState.getParcelable("currentLocation");
		}

		setupMap(savedInstanceState);
		return view;
	}


	/**
	 * Save last known location.
	 */
	@Override
	public void onSaveInstanceState(Bundle outState){
		outState.putParcelable("currentLocation", currentLocation);
		super.onSaveInstanceState(outState);
	}



	@Override
	public void onStop(){
		super.onStop();
		savedLocation = currentLocation;
	}



	/**
	 * Setup map.
	 */
	private void setupMap(Bundle savedInstanceState){
		mapView.onCreate(savedInstanceState);
		mapView.onResume();
		mapView.getMapAsync(this);
	}


	/**
	 * setup map after ready called.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap){
		map = googleMap;
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setZoomGesturesEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		int top = (int)getResources().getDimension(R.dimen.map_padding_top);
		int side = (int)getResources().getDimension(R.dimen.map_padding_side);
		map.setPadding(side, top, side, top);

		//add map move listener, invoke after map stop moving
		map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener(){
			@Override
			public void onCameraIdle(){
				CameraPosition cameraPosition = map.getCameraPosition();
				currentLocation = cameraPosition.target;
				callback.onMapIdle(currentLocation);
			}
		});

		moveMapToLocation();
		updateMapMarkers();
	}


	/**
	 * Invokes after click on place on list.
	 */
	public void animateToPlace(Place place){
		Marker placeMarker = placeMarkers.get(place.id);
		LatLng latLng;
		if (placeMarker != null){
			latLng = placeMarker.getPosition();
			placeMarker.showInfoWindow();
		}else
			latLng = new LatLng(place.geometry.location.lat, place.geometry.location.lng);

		map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 19.0f)));

	}


	/**
	 * Update map with most updated location of user.
	 */
	public void onLocationChanged(LatLng location){
		currentLocation = location;

		if (map != null){
			if (savedLocation == null)
				moveMapToLocation(currentLocation);
			else
				moveMapToLocation(savedLocation);
		}
	}


	/**
	 * Update map with latest places near map center. Add only new places,
	 * remove old places that not appear in the new list.
	 */
	public void updateMapPlaces(List<Place> places){

		BitmapDescriptor genericPlaceIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp);
		Set<String> placeIds = new HashSet<>();
		for (Place place : places){

			placeIds.add(place.id);
			if (placeMarkers.containsKey(place.id))
				continue;

			LatLng latLng = new LatLng(place.geometry.location.lat, place.geometry.location.lng);
			MarkerOptions markerOptions = new MarkerOptions().position(latLng)
					.title(place.name)
					.icon(genericPlaceIcon);

			markerOptionsList.put(place.id, markerOptions);
		}

		if (map != null)
			updateMapMarkers();

		//remove old markers
		List<String> markersToRemove = new ArrayList<>();
		for (Map.Entry<String, Marker> entry : placeMarkers.entrySet()){

			if (!placeIds.contains(entry.getKey())){
				markersToRemove.add(entry.getKey());
				entry.getValue().remove();
			}
		}

		for (String key : markersToRemove){
			Marker marker = placeMarkers.remove(key);
			marker.remove();
		}
	}

	private synchronized void updateMapMarkers(){
		if (markerOptionsList.isEmpty()) return;

		for (Map.Entry<String, MarkerOptions> entry : markerOptionsList.entrySet()){
			Marker marker = map.addMarker(entry.getValue());
			placeMarkers.put(entry.getKey(), marker);
		}
		markerOptionsList.clear();
	}


	/**
	 * Move map to new location depend on data.
	 * When activity first time open: need to move to current location.
	 * when activity first time open but map user's location is not detected yet (or did not grant permission)
	 * center map on tel-aviv.
	 */
	private void moveMapToLocation(){

		if (savedLocation != null){
			moveMapToLocation(savedLocation);

		}else if (currentLocation != null){
			moveMapToLocation(currentLocation);
		} else{
			if (map == null) return;

			LatLng TEL_AVIV = new LatLng(32.080708, 34.780592);
			map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(TEL_AVIV, 10.0f)));
		}
	}


	/**
	 * Move map to given new {@link LatLng} location.
	 */
	public void moveMapToLocation(LatLng latLng){
		if (latLng == null || map == null) return;

		currentLocation = latLng;
		map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 17.0f)));
	}


	/**
	 * Show my location indication on map (the blue dot).
	 */
	public void setMyLocationEnabled(boolean enabled){
		if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

			if (map != null)
				map.setMyLocationEnabled(enabled);
		}
	}


	/**
	 * Show my location map button.
	 */
	public void setMyLocationButtonEnabled(boolean enabled){
		if (map != null)
			map.getUiSettings().setMyLocationButtonEnabled(enabled);
	}


	/**
	 * Get current map center {@link LatLng}.
	 */
	public LatLng getCurrentLocation(){
		return currentLocation;
	}


	/**
	 * Map move listener.
	 */
	public interface MapMoveListener{


		/**
		 * Invoke after map movment stopped (after dragging manually or click on my location map button).
		 */
		void onMapIdle(LatLng currentLocation);
	}

}
