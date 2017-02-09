package com.places.di;

import dagger.Component;
import com.places.model.api.GoogleMapsApiImpl;



/**
 * @author Gilad Opher
 */
@PerApi
@Component(
		dependencies = {AppComponent.class},
		modules = {ApiModule.class}
)
public interface ApiComponent{


	GoogleMapsApiImpl googleMapsApiImpl();


}
