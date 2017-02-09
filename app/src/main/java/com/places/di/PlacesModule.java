package com.places.di;

import dagger.Module;
import dagger.Provides;
import com.places.model.api.GoogleMapsApiImpl;
import com.places.model.PlacesDataSource;
import com.places.model.PlacesRepository;
import com.places.model.remote.RemotePlacesDataSource;
import com.places.presenter.NearbyPlacesPresenter;



/**
 * @author Gilad Opher
 */
@Module
public class PlacesModule{


	@Provides
	PlacesDataSource providePlacesRemoteRepository(GoogleMapsApiImpl api){
		return new RemotePlacesDataSource(api);
	}


	@Provides
	PlacesRepository providePlacesRepository(RemotePlacesDataSource remotePlacesDataSource){
		return new PlacesRepository(remotePlacesDataSource);
	}


	@Provides
	NearbyPlacesPresenter provideNearbyPlacesPresenter(PlacesRepository repository){
		return new NearbyPlacesPresenter(repository);
	}

}
