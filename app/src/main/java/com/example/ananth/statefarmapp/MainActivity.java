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

import com.example.ananth.statefarmapp.models.Feature;
import com.example.ananth.statefarmapp.models.FemaHousingOwnersDamageResponse;
import com.example.ananth.statefarmapp.models.HousingAssistanceOwner;
import com.example.ananth.statefarmapp.models.IowaResponse;
import com.example.ananth.statefarmapp.models.OSMResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

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
    @Inject
    @Named("IowaClient")
    Retrofit iowaRetrofit;
    @Inject
    @Named("NomOSMClient")
    Retrofit osmRetrofit;
    private BottomSheetBehavior bottomSheetBehavior;
    private FEMAService femaService;
    private IOWAService iowaService;
    private NomOSMService osmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerMainActivityComponent.builder().networkComponent(((StateFarmCompetitionApplication) getApplication()).getNetworkComponent()).build().inject(this);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
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
                    Util.hideKeyboard(MainActivity.this);
                    searchProgressBar.setVisibility(View.VISIBLE);
                    handled = true;
                    //mMap.clear();

                    osmService.getAddressBySearch(searchText.getText().toString()).subscribeOn(Schedulers.io()).subscribe(new DisposableObserver<Response<List<OSMResponse>>>() {
                        @Override
                        public void onNext(final Response<List<OSMResponse>> listAddresses) {
                            if (!listAddresses.isSuccessful()) {
                                Log.v("osm", listAddresses.raw().request().url().toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchProgressBar.setVisibility(View.GONE);
                                    }
                                });
                                return;
                            }
                            if (listAddresses.body().size() > 0) {
                                final OSMResponse response = listAddresses.body().get(0);
                                final com.example.ananth.statefarmapp.models.Address address = listAddresses.body().get(0).getAddress();

                                final LatLng searchPoint = new LatLng(Double.parseDouble(response.getLat()), Double.parseDouble(response.getLon()));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMap.addMarker(new MarkerOptions().position(searchPoint).title(v.getText().toString()));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchPoint, 10));
                                    }
                                });

                                if (Util.currentSearch == Util.SearchState.ZIP_CODE) {
                                    if (address.getPostcode() == null) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                zipCodeText.setText(response.getDisplayName());
                                                damageText.setText("");
                                                Toast.makeText(getApplicationContext(), "Couldn't get FEMA data by zip code", Toast.LENGTH_LONG).show();
                                                adapter.setmDataset(new ArrayList<HousingAssistanceOwner>());
                                                searchProgressBar.setVisibility(View.GONE);
                                                findViewById(R.id.bottomSheet).setVisibility(View.VISIBLE);
                                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                            }
                                        });
                                        return;
                                    }
                                } else if (Util.currentSearch == Util.SearchState.CITY) {
                                    if (address.getCity() == null) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                zipCodeText.setText(response.getDisplayName());
                                                damageText.setText("");
                                                Toast.makeText(getApplicationContext(), "Couldn't get FEMA data by city", Toast.LENGTH_LONG).show();
                                                adapter.setmDataset(new ArrayList<HousingAssistanceOwner>());
                                                searchProgressBar.setVisibility(View.GONE);
                                                findViewById(R.id.bottomSheet).setVisibility(View.VISIBLE);
                                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                            }
                                        });
                                        return;
                                    }
                                }
                                String searchString = "";
                                if (Util.currentSearch == Util.SearchState.ZIP_CODE) {
                                    searchString = "zipCode%20eq%20" + address.getPostcode();
                                } else if (Util.currentSearch == Util.SearchState.CITY) {
                                    //$filter=city%20eq%20%27HOUSTON%27%20and%20state%20eq%20%27TX%27
                                    searchString = "city%20eq%20%27" + address.getCity().toUpperCase() + "%27%20and%20state%20eq%20%27" + Util.states.get(address.getState()) + "%27";
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
                                                        if (Util.currentSearch == Util.SearchState.ZIP_CODE) {
                                                            zipCodeText.setText(address.getPostcode());
                                                        } else if (Util.currentSearch == Util.SearchState.CITY) {
                                                            zipCodeText.setText(address.getCity());
                                                        }
                                                        double totalCost = 0;
                                                        double paidCost = 0;
                                                        for (HousingAssistanceOwner owner : owners) {
                                                            totalCost += owner.getTotalDamage();
                                                            paidCost += owner.getTotalApprovedIhpAmount();
                                                        }
                                                        damageText.setText(owners.size() + " damage records totalling $" + Util.formatter.format(totalCost) + " \n with $" + Util.formatter.format(paidCost) + " paid out");
                                                        adapter.setmDataset(owners);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                findViewById(R.id.bottomSheet).setVisibility(View.VISIBLE);
                                                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                                            }
                                                        });
                                                    } else {
                                                        if (Util.currentSearch == Util.SearchState.ZIP_CODE) {
                                                            zipCodeText.setText(address.getPostcode());
                                                        } else if (Util.currentSearch == Util.SearchState.CITY) {
                                                            zipCodeText.setText(address.getCity());
                                                        }
                                                        damageText.setText("No damage records found");
                                                        adapter.setmDataset(new ArrayList<HousingAssistanceOwner>());
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                findViewById(R.id.bottomSheet).setVisibility(View.VISIBLE);
                                                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                                            }
                                                        });
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
                        }

                        @Override
                        public void onError(final Throwable e) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Couldn't find place", Toast.LENGTH_LONG).show();
                                    searchProgressBar.setVisibility(View.GONE);
                                    Log.v("osm", "error " + e.getMessage());
                                    e.printStackTrace();
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


                    //Util.getAddressFromAddressString(searchText.getText().toString(), getApplicationContext()).subscribe();

                }
                return handled;
            }
        });
        femaService = femaRetrofit.create(FEMAService.class);
        osmService = osmRetrofit.create(NomOSMService.class);
        iowaService = iowaRetrofit.create(IOWAService.class);
        iowaService.getIowaCrashData().subscribeOn(Schedulers.io()).subscribe(new DisposableObserver<Response<IowaResponse>>() {
            @Override
            public void onNext(final Response<IowaResponse> iowaResponseResponse) {
                if (iowaResponseResponse.isSuccessful()) {
                    Log.v("iowa", "successful");
                    final List<LatLng> crashList = new ArrayList<>();

                    for (Feature feature : iowaResponseResponse.body().getFeatures()) {
                        crashList.add(new LatLng(feature.getGeometry().getY(), feature.getGeometry().getX()));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                                    .data(crashList)
                                    .build();
                            mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(crashList.get(0), 10);
                            //mMap.animateCamera(cameraUpdate);
                        }
                    });

                } else {
                    Log.v("iowa", "failed");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.v("iowa", "error " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        findViewById(R.id.bottomSheet).setVisibility(View.GONE);
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(400);
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
