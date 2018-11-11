package com.example.ananth.statefarmapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ananth.statefarmapp.models.FemaHousingOwnersDamageResponse;
import com.example.ananth.statefarmapp.models.HousingAssistanceOwner;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

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
    private BottomSheetBehavior bottomSheetBehavior;
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
        final TextView zipCodeText = findViewById(R.id.zipText);
        final TextView damageText = findViewById(R.id.averageDamageText);
        final RecyclerView damageRecyclerView = findViewById(R.id.damageRecyclerView);
        final ProgressBar searchProgressBar = findViewById(R.id.searchProgress);
        searchProgressBar.setVisibility(View.GONE);
        damageRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        final FemaHousingAssistenceAdapter adapter = new FemaHousingAssistenceAdapter();
        damageRecyclerView.setAdapter(adapter);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchProgressBar.setVisibility(View.VISIBLE);
                    handled = true;
                    mMap.clear();
                    Util.getAddressFromAddressString(searchText.getText().toString(), getApplicationContext()).subscribe(new DisposableObserver<Address>() {
                        @Override
                        public void onNext(final Address address) {
                            if (address == null) {
                                Toast.makeText(getApplicationContext(), "Couldn't find place", Toast.LENGTH_LONG).show();
                            }
                            LatLng searchPoint = new LatLng((address.getLatitude()),
                                    (address.getLongitude()));

                            mMap.addMarker(new MarkerOptions().position(searchPoint).title(v.getText().toString()));

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchPoint, 10));
                            if (Util.currentSearch == Util.SearchState.ZIP_CODE) {
                                if (address.getPostalCode() == null) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            zipCodeText.setText(address.getFeatureName());
                                            damageText.setText("");
                                            Toast.makeText(getApplicationContext(), "Couldn't get FEMA data", Toast.LENGTH_LONG).show();
                                            searchProgressBar.setVisibility(View.GONE);
                                        }
                                    });
                                    return;
                                }
                            } else if (Util.currentSearch == Util.SearchState.CITY) {
                                if (address.getLocality() == null) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            zipCodeText.setText(address.getFeatureName());
                                            damageText.setText("");
                                            Toast.makeText(getApplicationContext(), "Couldn't get FEMA data", Toast.LENGTH_LONG).show();
                                            searchProgressBar.setVisibility(View.GONE);
                                        }
                                    });
                                    return;
                                }
                            }
                            String searchString = "";
                            if (Util.currentSearch == Util.SearchState.ZIP_CODE) {
                                searchString = "zipCode%20eq%20" + address.getPostalCode();
                            } else if (Util.currentSearch == Util.SearchState.CITY) {
                                searchString = "city%20eq%20%27"+address.getLocality().toUpperCase()+"%27";
                            }
                            femaService.getFemaHousingOwnersDamage(searchString).subscribeOn(Schedulers.io()).subscribe(new DisposableObserver<Response<FemaHousingOwnersDamageResponse>>() {
                                @Override
                                public void onNext(final Response<FemaHousingOwnersDamageResponse> femaHousingOwnersDamageResponse) {
                                    if (femaHousingOwnersDamageResponse.isSuccessful()) {
                                        Log.v("fema", "recievedData:" + femaHousingOwnersDamageResponse.body().getMetadata().getUrl());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                List<HousingAssistanceOwner> owners = femaHousingOwnersDamageResponse.body().getHousingAssistanceOwners();
                                                if (owners.size() > 0) {
                                                    if(Util.currentSearch == Util.SearchState.ZIP_CODE){
                                                        zipCodeText.setText(address.getPostalCode());
                                                    }
                                                    else if(Util.currentSearch == Util.SearchState.CITY){
                                                        zipCodeText.setText(address.getLocality());
                                                    }
                                                    damageText.setText(owners.size() + " damage records");
                                                    adapter.setmDataset(owners);
                                                    findViewById(R.id.bottomSheet).setVisibility(View.VISIBLE);
                                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                                } else {
                                                    if(Util.currentSearch == Util.SearchState.ZIP_CODE){
                                                        zipCodeText.setText(address.getPostalCode());
                                                    }
                                                    else if(Util.currentSearch == Util.SearchState.CITY){
                                                        zipCodeText.setText(address.getLocality());
                                                    }
                                                    damageText.setText("No damage records found");
                                                    adapter.setmDataset(new ArrayList<HousingAssistanceOwner>());
                                                }
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        searchProgressBar.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Log.v("fema", femaHousingOwnersDamageResponse.raw().request().url().toString());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                searchProgressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.v("error", "error getting data");
                                    Log.v("error", "error: " + e.getMessage());

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Error getting data", Toast.LENGTH_LONG).show();
                                            searchProgressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }

                                @Override
                                public void onComplete() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            searchProgressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(), "Couldn't find place", Toast.LENGTH_LONG).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }

                        @Override
                        public void onComplete() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    });

                }
                return handled;
            }
        });
        femaService = femaRetrofit.create(FEMAService.class);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        findViewById(R.id.bottomSheet).setVisibility(View.GONE);
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(200);
        FloatingActionButton searchSelectFAB = findViewById(R.id.selectSearchSettingFAB);
        searchSelectFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeSearchDialog();
            }
        });

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

    private void showChangeSearchDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialog_layout = inflater.inflate(R.layout.select_search_dialog, (ViewGroup) findViewById(R.id.dialogRootLayout));
        AlertDialog.Builder db = new AlertDialog.Builder(MainActivity.this);
        db.setView(dialog_layout);
        db.setTitle("Search By:");
        db.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final Dialog dialog = db.create();


        RadioGroup radioGroup = dialog_layout.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.zipRadio:
                        Util.currentSearch = Util.SearchState.ZIP_CODE;
                        break;
                    case R.id.cityRadio:
                        Util.currentSearch = Util.SearchState.CITY;
                        break;
                }
            }
        });
        if (Util.currentSearch == Util.SearchState.ZIP_CODE) {
            radioGroup.check(R.id.zipRadio);
        } else if (Util.currentSearch == Util.SearchState.CITY) {
            radioGroup.check(R.id.cityRadio);
        }
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }
}
