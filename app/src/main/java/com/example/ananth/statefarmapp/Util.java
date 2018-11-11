package com.example.ananth.statefarmapp;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Callable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Util {
    enum SearchState {
        CITY, ZIP_CODE
    }

    public static SearchState currentSearch = SearchState.ZIP_CODE;
    public static final DecimalFormat formatter = new DecimalFormat("#,###.00");
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
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus()!=null){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

    }

    //Ty to the person on stackoverflow who made this
    //https://stackoverflow.com/a/11006236/4833158
    public static final Map<String, String> states = new HashMap<String, String>();
    public static void initStateMap(){
        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","QC");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");
    }
}
