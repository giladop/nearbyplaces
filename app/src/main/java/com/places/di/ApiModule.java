package com.places.di;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import com.places.model.api.GoogleMapsApi;
import com.places.model.api.GoogleMapsApiImpl;



/**
 * @author Gilad Opher
 */
@Module
public class ApiModule{


	@Provides
	GoogleMapsApi provideGoogleMapsApi(Retrofit retrofit){
		return retrofit.create(GoogleMapsApi.class);
	}


	@Provides
	GoogleMapsApiImpl provideGoogleMapsApiImpl(GoogleMapsApi api){
		return new GoogleMapsApiImpl(api);
	}

}
