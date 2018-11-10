package com.example.ananth.statefarmapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ananth.statefarmapp.models.FemaHousingOwnersDamageResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.annotation.Retention;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private final int ACCESS_FINE_LOCATION_CODE = 100;
    @Inject
    @Named("FEMAClient")
    Retrofit femaRetrofit;

    private FEMAService femaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerMainActivityComponent.builder().networkComponent(((StateFarmCompetitionApplication) getApplication()).getNetworkComponent()).build().inject(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION_CODE);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        ACCESS_FINE_LOCATION_CODE);
            }
        }
        final EditText searchText = findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handled = true;
                    mMap.clear();
                    Util.getAddressFromAddressString(searchText.getText().toString(),getApplicationContext()).subscribe(new DisposableObserver<Address>() {
                        @Override
                        public void onNext(Address address) {
                            if (address == null) {
                                Toast.makeText(getApplicationContext(), "Couldn't find place", Toast.LENGTH_LONG).show();
                            }
                            LatLng searchPoint =  new LatLng((address.getLatitude()),
                                    (address.getLongitude()));

                            mMap.addMarker(new MarkerOptions().position(searchPoint).title(v.getText().toString()));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchPoint, 10));
                            femaService.getFemaHousingOwnersDamage("zipCode%20eq%20"+address.getPostalCode()).subscribeOn(Schedulers.io()).subscribe(new DisposableObserver<Response<FemaHousingOwnersDamageResponse>>() {
                                @Override
                                public void onNext(Response<FemaHousingOwnersDamageResponse> femaHousingOwnersDamageResponse) {
                                    if(femaHousingOwnersDamageResponse.isSuccessful()) {
                                        Log.v("fema", "recievedData:" + femaHousingOwnersDamageResponse.body().getMetadata().getUrl());
                                    }
                                    else{
                                        Log.v("fema",femaHousingOwnersDamageResponse.raw().request().url().toString());
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.v("error","error getting data");
                                    Log.v("error","error: "+e.getMessage());

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(), "Couldn't find place", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

                }
                return handled;
            }
        });
        femaService = femaRetrofit.create(FEMAService.class);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1000f, this);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

    }
}
