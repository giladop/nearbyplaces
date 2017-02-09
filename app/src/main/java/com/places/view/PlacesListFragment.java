package com.places.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.places.R;
import com.places.model.data.Place;



/**
 * A fragment showing list of nearby {@link Place}, in addition showing the name of current map center location.
 *
 * @author Gilad Opher
 */
public class PlacesListFragment extends Fragment{


	@BindView(R.id.nearby_address_name)
	TextView nearByAddressName;


	@BindView(R.id.nearby_address_progress_bar)
	ProgressBar nearbyAddressProgressBar;


	@BindView(R.id.places_progress_bar)
	ProgressBar nearbyPlacesProgressBar;


	@BindView(R.id.places_list)
	RecyclerView placesList;


	/**
	 * A callback for notifying about list item click event.
	 */
	private PlacesListListener callback;


	@Override
	public void onAttach(Context context){
		super.onAttach(context);
		if (context instanceof PlacesListListener)
			callback = (PlacesListListener)context;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.places_list_layout, container, false);
		ButterKnife.bind(this, view);

		placesList.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		placesList.setLayoutManager(linearLayoutManager);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
		placesList.addItemDecoration(dividerItemDecoration);

		return view;
	}


	/**
	 * show loading indicator for geocode loading.
	 */
	public void setAddressProgressBarVisibility(boolean visible){
		nearbyAddressProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}


	/**
	 * show loading indicator for places loading.
	 */
	public void setPlacesProgressBarVisibility(boolean visible){
		nearbyPlacesProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}


	/**
	 * Show empty state message.
	 */
	public void onPlacesNotAvailable(){
		setPlaces(new ArrayList<Place>());
		nearbyPlacesProgressBar.setVisibility(View.INVISIBLE);
		Toast.makeText(getActivity(), R.string.no_places_found, Toast.LENGTH_SHORT).show();
	}


	/**
	 * replace new places with old ones.
	 */
	public void setPlaces(List<Place> places){
		NearbyPlacesAdapter adapter = new NearbyPlacesAdapter(getActivity(), places, new NearbyPlacesAdapter.PlaceItemCallback(){
			@Override
			public void onPlaceClicked(Place place){
				callback.onPlaceClicked(place);
			}
		});
		placesList.setAdapter(adapter);
	}


	/**
	 * Set geocode address name.
	 */
	public void setAddressName(String name){
		nearByAddressName.setText(name);
	}


	/**
	 * Places list listener.
	 */
	public interface PlacesListListener{


		/**
		 * Invoke when click on place.
		 */
		void onPlaceClicked(Place place);
	}


}
