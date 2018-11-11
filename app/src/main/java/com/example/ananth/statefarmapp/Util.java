package com.example.ananth.statefarmapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Util {
    enum SearchState {
        CITY, ZIP_CODE
    }

    public static SearchState currentSearch = SearchState.ZIP_CODE;

    public static LatLng getLocationFromAddress(String strAddress, Context context) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((location.getLatitude()),
                    (location.getLongitude()));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    public static io.reactivex.Observable<Address> getAddressFromAddressString(final String strAddress, final Context context) {
        return io.reactivex.Observable.fromCallable(new Callable<Address>() {
            @Override
            public Address call() {
                Geocoder coder = new Geocoder(context);
                List<Address> address;
                try {
                    address = coder.getFromLocationName(strAddress, 5);
                    if (address == null) {
                        return null;
                    }
                    Address location = address.get(0);
                    if (currentSearch == SearchState.ZIP_CODE) {
                        if (location.getPostalCode() != null) {
                            return location;
                        }
                    } else if (currentSearch == SearchState.CITY) {
                        if (location.getLocality() != null) {
                            return location;
                        }
                    }
                    if (!(location.hasLatitude() && location.hasLongitude())) {
                        return location;
                    }
                    List<Address> newAddresses = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                    if (currentSearch == SearchState.ZIP_CODE) {
                        if (newAddresses == null || newAddresses.size() == 0 || newAddresses.get(0).getPostalCode() == null) {
                            return location;
                        }
                    } else if (currentSearch == SearchState.CITY) {
                        if (newAddresses == null || newAddresses.size() == 0 || newAddresses.get(0).getLocality() == null) {
                            return location;
                        }
                    }

                    return newAddresses.get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }
}
