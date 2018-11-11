package com.example.ananth.statefarmapp;

import android.app.Application;

public class StateFarmCompetitionApplication extends Application {
    private NetworkComponent networkComponent;
    private final String femaBaseURL = "https://www.fema.gov/api/open/v1/";
    private final String iowaBaseURL = "https://services.arcgis.com/";
    private final String nomOSMBaseURL = "https://nominatim.openstreetmap.org/search/";
    @Override
    public void onCreate() {
        super.onCreate();
        networkComponent = DaggerNetworkComponent.builder().fEMANetworkModule(new FEMANetworkModule(femaBaseURL)).iowaNetworkModule(new IowaNetworkModule(iowaBaseURL)).nominatimOSMNetworkModule(new NominatimOSMNetworkModule(nomOSMBaseURL)).build();
        Util.initStateMap();
    }
    public NetworkComponent getNetworkComponent(){
        return networkComponent;
    }
}
