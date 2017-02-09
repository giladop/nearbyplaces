package com.places.di;

import com.places.view.NearbyPlacesActivity;

import dagger.Component;



/**
 * @author Gilad Opher
 */
@ActivityScoped
@Component(
		dependencies = {ApiComponent.class},
		modules = {PlacesModule.class}
)
public interface PlacesComponent{


	void inject(NearbyPlacesActivity activity);


}
