package com.example.ananth.statefarmapp;

import android.app.Application;

public class StateFarmCompetitionApplication extends Application {
    private NetworkComponent networkComponent;
    private final String femaBaseURL = "";
    private final String osmBaseURL = "";
    @Override
    public void onCreate() {
        super.onCreate();
        networkComponent = DaggerNetworkComponent.builder().fEMANetworkModule(new FEMANetworkModule(femaBaseURL)).oSMNetworkModule(new OSMNetworkModule(osmBaseURL)).build();
    }
    public NetworkComponent getNetworkComponent(){
        return networkComponent;
    }
}
