package com.places.di;

import com.places.model.repository.NearbyPlacesRepository;
import com.places.model.api.GoogleMapsApiImpl;
import com.places.model.repository.RemoteRepository;
import com.places.model.repository.RemotePlacesRepository;
import com.places.presenter.NearbyPlacesPresenter;

import dagger.Module;
import dagger.Provides;



/**
 * @author Gilad Opher
 */
@Module
public class PlacesModule{


	@Provides
	RemoteRepository providePlacesRemoteRepository(GoogleMapsApiImpl api){
		return new RemotePlacesRepository(api);
	}


	@Provides
	NearbyPlacesRepository providePlacesRepository(RemotePlacesRepository remotePlacesRepository){
		return new NearbyPlacesRepository(remotePlacesRepository);
	}


	@Provides
	NearbyPlacesPresenter provideNearbyPlacesPresenter(NearbyPlacesRepository nearbyPlacesRepository){
		return new NearbyPlacesPresenter(nearbyPlacesRepository);
	}

}
