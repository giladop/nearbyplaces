package com.places.di;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;



/**
 * @author Gilad Opher
 */
@Singleton
@Component(
		modules = {AppModule.class}
)
public interface AppComponent{


	Gson gson();


	Retrofit retrofit();


	Context context();


	Picasso picasso();

}
