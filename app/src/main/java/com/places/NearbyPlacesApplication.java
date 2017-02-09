package com.places;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.places.di.*;
import com.places.di.AppComponent;



/**
 * The PLaces {@link Application}. Initiate and save the {@link AppComponent}.
 *
 * @author Gilad Opher
 */
public class NearbyPlacesApplication extends Application{


	/**
	 * The {@link AppComponent}.
 	 */
	private AppComponent appComponent;


	@Override
	public void onCreate(){
		super.onCreate();
		this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(getApplicationContext())).build();
	}


	/**
	 * static access to {@link NearbyPlacesApplication}.
	 */
	public static NearbyPlacesApplication getNearbyPlacesApplication(@NonNull Context context){
		return ((NearbyPlacesApplication)context.getApplicationContext());
	}


	/**
	 * Return {@link AppComponent}.
	 */
	public AppComponent getAppComponent(){
		return appComponent;
	}


	/**
	 * Create and return {@link ApiComponent}.
	 */
	public ApiComponent getApiComponent(){
		return DaggerApiComponent.builder().appComponent(appComponent).apiModule(new ApiModule()).build();
	}


}
