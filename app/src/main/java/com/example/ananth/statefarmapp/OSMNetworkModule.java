package com.example.ananth.statefarmapp;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class OSMNetworkModule {
    public final String BASE_URL;
    public OSMNetworkModule(String baseUrl){
        BASE_URL = baseUrl;
    }
    @Provides
    @Singleton
    @Named("OSMClient")
    Retrofit getClient(){
        return new Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
