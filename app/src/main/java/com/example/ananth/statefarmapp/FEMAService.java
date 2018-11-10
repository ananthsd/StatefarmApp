package com.example.ananth.statefarmapp;

import com.example.ananth.statefarmapp.models.FemaHousingOwnersDamageResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FEMAService {
    @GET("/api/open/v1/HousingAssistanceOwners")
    Observable<Response<FemaHousingOwnersDamageResponse>> getFemaHousingOwnersDamage(@Query(value="$filter", encoded=true) String zip);
}
