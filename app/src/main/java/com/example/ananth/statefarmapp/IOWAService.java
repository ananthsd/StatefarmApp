package com.example.ananth.statefarmapp;

import com.example.ananth.statefarmapp.models.FemaHousingOwnersDamageResponse;
import com.example.ananth.statefarmapp.models.IowaResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOWAService {
    @GET("/8lRhdTsQyJpO52F1/arcgis/rest/services/Crash_Data_View/FeatureServer/0/query?where=1%3D1&outFields=XCOORD,YCOORD&outSR=4326&f=json")
    Observable<Response<IowaResponse>> getIowaCrashData();
}
