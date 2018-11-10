package com.example.ananth.statefarmapp;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
@Singleton
@Component(modules = {FEMANetworkModule.class, OSMNetworkModule.class, AppModule.class})
public interface NetworkComponent {
    @Named("FEMAClient")
    Retrofit FEMARetrofit();
    @Named("OSMClient")
    Retrofit OSMRetrofit();
}
