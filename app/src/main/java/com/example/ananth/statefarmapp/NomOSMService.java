package com.example.ananth.statefarmapp;

import com.example.ananth.statefarmapp.models.IowaResponse;
import com.example.ananth.statefarmapp.models.OSMResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NomOSMService {
    @GET("?format=json&polygon=1&addressdetails=1")
    Observable<Response<List<OSMResponse>>> getAddressBySearch(@Query(value = "q", encoded = false) String query);
}
