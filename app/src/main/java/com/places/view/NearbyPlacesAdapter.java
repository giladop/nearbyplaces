package com.places.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.places.NearbyPlacesApplication;
import com.places.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.places.model.data.Place;


/**
 * The places adapter.
 *
 * @author Gilad Opher
 */
class NearbyPlacesAdapter extends RecyclerView.Adapter<NearbyPlacesAdapter.PlaceViewHolder>{


	/**
	 * PLaces data list.
	 */
	private List<Place> places;


	/**
	 * image loader.
	 */
	private Picasso picasso;


	/**
	 * Place item click callback.
	 */
	private PlaceItemCallback callback;


	NearbyPlacesAdapter(Context context, List<Place> places, PlaceItemCallback callback){
		this.places = places;
		this.picasso = NearbyPlacesApplication.getNearbyPlacesApplication(context).getAppComponent().picasso();
		this.callback = callback;
	}


	/**
	 * The item view click listener.
	 */
	private final View.OnClickListener clickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v){
			PlaceViewHolder placeViewHolder = (PlaceViewHolder)v.getTag();
			onItemClick(placeViewHolder);
		}
	};


	/**
	 * Invoke when click on place item on list.
	 */
	private void onItemClick(PlaceViewHolder placeViewHolder){
		if (callback == null)
			return;

		int position = placeViewHolder.getAdapterPosition();
		if (position == RecyclerView.NO_POSITION)
			return;

		Place place = places.get(position);
		if (place != null)
			callback.onPlaceClicked(place);
	}


	@Override
	public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_row_layout, parent, false);
		view.setOnClickListener(clickListener);
		PlaceViewHolder holder = new PlaceViewHolder(view);
		view.setTag(holder);

		return holder;
	}


	@Override
	public void onBindViewHolder(PlaceViewHolder holder, int position){
		Place place = places.get(position);

		holder.placeName.setText(place.name);
		picasso.load(place.imageUrl).into(holder.placeImageType);
	}


	@Override
	public int getItemCount(){
		return places != null ? places.size() : 0;
	}


	/**
	 * The place {@link RecyclerView.ViewHolder}.
	 */
	class PlaceViewHolder extends RecyclerView.ViewHolder{


		@BindView(R.id.place_image_type)
		ImageView placeImageType;


		@BindView(R.id.place_name)
		TextView placeName;


		PlaceViewHolder(View itemView){
			super(itemView);

			ButterKnife.bind(this, itemView);
		}
	}


	/**
	 * A callback for external use
	 */
	interface PlaceItemCallback{


		/**
		 * Invoke when click on place item.
		 */
		void onPlaceClicked(Place place);
	}


}
