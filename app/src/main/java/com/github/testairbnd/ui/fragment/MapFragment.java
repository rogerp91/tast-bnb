package com.github.testairbnd.ui.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.roger91.mlprogress.MlProgress;
import com.github.testairbnd.R;
import com.github.testairbnd.contract.MapContract;
import com.github.testairbnd.data.model.Locality;
import com.github.testairbnd.util.PlayServices;
import com.github.testairbnd.util.Usefulness;
import com.github.testairbnd.util.ViewMarker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.GraphRequest.TAG;
import static com.github.testairbnd.R.id.map;
import static com.github.testairbnd.service.ManagerService.LOCATION_DEFAULT_FASTEST_INTERVAL;
import static com.github.testairbnd.service.ManagerService.LOCATION_DEFAULT_INTERVAL;
import static com.github.testairbnd.util.Constants.INTENT_ACTION_NOT_GPS_ACTIVE;
import static com.github.testairbnd.util.Constants.INTENT_ACTION_PERMISSION_FAILED;
import static com.github.testairbnd.util.Constants.INTENT_ACTION_PERMISSION_SUCCESS;

/**
 * Created by roger on 07/12/16.
 */

public class MapFragment extends BaseFragment implements MapContract.View,
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @BindView(R.id.progress)
    MlProgress mProgressView;

    //Error
    @BindView(R.id.layout_message)
    RelativeLayout layout_message;
    @BindView(R.id.text_message)
    TextView text_message;

    //Permission
    @BindView(R.id.layout_message_per)
    RelativeLayout layout_message_per;
    @BindView(R.id.text_message_per)
    TextView text_message_per;

    //GPS
    @BindView(R.id.layout_message_gps)
    RelativeLayout layout_message_gps;
    @BindView(R.id.text_message_gps)
    TextView text_message_gps;

    //Location
    @BindView(R.id.layout_message_loc)
    RelativeLayout layout_message_loc;
    @BindView(R.id.text_message_loc)
    TextView text_message_loc;

    @BindView(R.id.container_map)
    FrameLayout container_map;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

    @Inject
    MapContract.Presenter presenter;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Location locationAux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        injectView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Presenter
        presenter.setView(this);

        // Map
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
        if (mapFragment == null && getActivity() != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(map, mapFragment).commit();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        buildGoogleApiClient();
        init();// Location
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdate();// Location
        if (mGoogleApiClient != null) {// Location
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);
            mGoogleApiClient.disconnect();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PlayServices.isGooglePlayServicesAvailable(getActivity().getApplicationContext())) {
            Usefulness.showMessage(getView(), "Google Play Services required to use the app", Snackbar.LENGTH_LONG);
            getActivity().finish();
            return;
        }
        presenter.showViewProgress();
        presenter.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_ACTION_PERMISSION_SUCCESS);
        filter.addAction(INTENT_ACTION_NOT_GPS_ACTIVE);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (RuntimeException e) {
            Log.e(TAG, "onPause: " + e.getMessage());
        }
    }

    @Override
    public void setTitle() {
//    ab.setTitle("Map");
    }

    @Override
    public void showProgress(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showModels(List<Locality> localities) {
        List<MarkerOptions> markerOptionses = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int x = 0; x < localities.size(); x++) {
            // View for card with price
            View markerView = ((LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
            TextView textView = (TextView) markerView.findViewById(R.id.price);
            textView.setText(localities.get(x).getPrice() + " " + localities.get(x).getLocalized_currency());

            // Add Market
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(localities.get(x).getLatitud(), localities.get(x).getLongitud()))
                    .icon(BitmapDescriptorFactory.fromBitmap(ViewMarker.createDrawableFromView(getActivity(), markerView)));
            markerOptionses.add(marker);
        }

        // Mapper Market
        for (int i = 0; i < markerOptionses.size(); i++) {
            mMap.addMarker(markerOptionses.get(i));
            builder.include(markerOptionses.get(i).getPosition());
        }

        // Bounds Market
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);

    }

    @Override
    public void showNoModels(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message.setText(getString(R.string.no_data_available));
    }

    @Override
    public void showNetworkError(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message.setText(getString(R.string.no_connection));
    }

    @Override
    public void showErrorOcurred(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message.setText(getString(R.string.error_occurred));
    }

    @Override
    public void showErrorNotSolve(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message.setText(getString(R.string.error_many));
    }

    @Override
    public void showMessage(String message) {
        Usefulness.showMessage(getView(), message, Snackbar.LENGTH_LONG);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /**
     * No GPS, {@link Intent} of @{@link BroadcastReceiver} from {@link com.github.testairbnd.ui.activity.MainActivity}
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(INTENT_ACTION_PERMISSION_FAILED)) {
                Log.d(TAG, "onReceive: ");
//        presenter.permissionFailed();
            } else {
                if (action.equals(INTENT_ACTION_NOT_GPS_ACTIVE)) {
                    presenter.notActiveGPS();
                }
            }
        }
    };

    @Override
    public void showActiveGPS(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message_gps.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message_gps.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message_gps.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message_gps.setText(getString(R.string.active_gps));
    }

    @Override
    public void showPermission(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message_per.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message_per.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message_per.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message_per.setText(getString(R.string.require_permission));
    }

    @Override
    public void showNoLocalitation(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message_loc.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message_loc.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message_loc.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message_loc.setText(getString(R.string.no_location));
    }

    @Override
    public void showMap(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        container_map.setVisibility(active ? View.VISIBLE : View.GONE);
        container_map.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container_map.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
    }

    @OnClick(R.id.text_try_again)
    void onClickTryAgain() {//Retry
        presenter.showViewProgress();
        presenter.findPosition(locationAux, true);
    }

    @OnClick(R.id.text_try_again_gps)
    void onClickTryAgainGPS() { //Retry
        presenter.showViewProgress();
        presenter.findPosition(locationAux, true);
    }

    @OnClick(R.id.btn_required_gps)
    void onClickRequiredGPS() {
        getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @OnClick(R.id.text_try_again_per)
    void onClickTryAgainPer() {//Retry
        presenter.showViewProgress();
        presenter.findPosition(locationAux, true);
    }

    @OnClick(R.id.btn_required_permission)
    void onClickRequiredPer() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getActivity().getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(i);
    }

    @OnClick(R.id.text_try_again_loc)
    void onClickTryAgainLoc() {//Retry
        presenter.showViewProgress();
        presenter.findPosition(locationAux, true);
    }

    @OnClick(R.id.btn_required_loc)
    void onClickRequiredLoc() {
        presenter.showViewProgress();
        presenter.failGetPosition();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG, "onConnected: " + mLastLocation.getAltitude());
        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            showMessage("Disconnected. Please re-connect.");
        } else if (i == CAUSE_NETWORK_LOST) {
            showMessage("Network lost. Please re-connect.");
        }
        init();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /**
     * Init request
     */
    private synchronized void initLocationRequest() {
        Log.d(TAG, "initLocationRequest: ");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_DEFAULT_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_DEFAULT_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * start request
     */
    private void startLocationUpdate() {
        Log.d(TAG, "startLocationUpdate: ");
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        Log.d(TAG, "stopLocationUpdate: ");
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Config location
     */
    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient: ");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addOnConnectionFailedListener(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
    }

    /**
     * Connect location
     */
    private void init() {
        Log.d(TAG, "init: ");
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationAux = location;
        try {
            Log.d(TAG, "onLocationChanged: " + location.getLatitude());
        } catch (NullPointerException e) {
            Log.d(TAG, "onLocationChanged: " + e.getMessage());
        }

        presenter.findPosition(locationAux, true);
    }

}