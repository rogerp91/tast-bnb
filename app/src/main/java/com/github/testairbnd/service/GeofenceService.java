package com.github.testairbnd.service;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.rogerp91.pref.SP;
import com.github.testairbnd.R;
import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.data.source.LodgingsDataSource;
import com.github.testairbnd.data.source.local.LodgingsLocalDataSource;
import com.github.testairbnd.data.source.remote.LodgingsRemoteDataSource;
import com.github.testairbnd.data.source.repository.LodgingsRepository;
import com.github.testairbnd.ui.activity.MainActivity;
import com.github.testairbnd.util.CalculateDistance;
import com.github.testairbnd.util.Devices;
import com.github.testairbnd.util.ShowTextRunnable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import static com.github.testairbnd.service.ManagerService.LOCATION_DEFAULT_FASTEST_INTERVAL;
import static com.github.testairbnd.service.ManagerService.LOCATION_DEFAULT_INTERVAL;

public class GeofenceService extends BaseService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    protected static String TAG = GeofenceService.class.getSimpleName();

    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    protected LocationManager locationManager;

    // Is running services?
    public static boolean INSTANCE = false;

    private Handler mHandler;
    private LocationThread mLocation = null;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = true;
        mHandler = new Handler();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        buildGoogleApiClient();
        SP.putInt(ManagerService.ENABLE, 1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (ManagerService.ACTION_START.equals(intent.getAction())) {
                start();
            } else {
                if (ManagerService.ACTION_ALIVE.equals(intent.getAction())) {
                    stop();
                }
            }
        } else {
            stopSelf();
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        INSTANCE = false;
        SP.putInt(ManagerService.ENABLE, 0);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
    }

    private void start() {
        //Log.d(TAG, "start: ");
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            requestPermission(0);
        } else {
            if (Devices.isMarshmallow()) {
                if (Devices.hasPermission(this, "android.permission.ACCESS_FINE_LOCATION") || Devices.hasPermission(this, "android.permission.ACCESS_COARSE_LOCATION")) {
                    init();
                } else {
                    requestPermission(1);
                }
            } else {
                init();
            }
        }
    }

    /**
     * Connect location
     */
    private void init() {
        //Log.d(TAG, "init: ");
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Stop
     */
    private void stop() {
        stopLocationUpdate();
        stopSelf();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(1);
            return;
        }
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            Log.d(TAG, "onConnected lat: " + mLocation.getLatitude());
            Log.d(TAG, "onConnected lng: " + mLocation.getLongitude() + "\n");
        }
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
        init();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        calculateLocation(location.getLatitude(), location.getLongitude());
    }

    /**
     * Init request
     */
    private synchronized void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_DEFAULT_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_DEFAULT_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * start request
     */
    private void startLocationUpdate() {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(1);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * stop location
     */
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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
    }

    /**
     * * Permits needed
     * 0 ManagerService.ACTION_SETTING_GPS
     * 1 ManagerService.ACTION_PERMISSION_GPS
     *
     * @param permision {@link android.Manifest.permission}
     */
    private void requestPermission(int permision) {
        String type = permision == 0 ? ManagerService.ACTION_SETTING_GPS : ManagerService.ACTION_PERMISSION_GPS;
        String msg = permision == 0 ? "Please enable location " : "Special location permission required";
        sendBroadcast(new Intent().setAction(type));
        mHandler.post(new ShowTextRunnable(msg, this));
        stopSelf();
    }

    private void calculateLocation(@NonNull Double latitude, @NonNull Double longitude) {
        try {
            if (mLocation == null) {
                mLocation = new LocationThread(latitude, longitude);
            } else {
                if (mLocation.isAlive()) {
                    mLocation = new LocationThread(latitude, longitude);
                }
            }
            mLocation.start();
        } catch (Exception e) {
            stop();
            ManagerService.actionStartLocation(getApplicationContext());
        }
    }

    /**
     * Thread location to server
     */
    private class LocationThread extends Thread {

        private Double latitude;
        private Double longitude;

        List<ListingDetail> listingDetailsAux;

        LocationThread(Double latitude, Double longitude) {
            Log.d(TAG, "LocationThread: ");
            this.latitude = latitude;
            this.longitude = longitude;
            listingDetailsAux = new ArrayList<>();
        }

        @Override
        public void run() {
            super.run();
            LodgingsDataSource local = new LodgingsLocalDataSource();
            LodgingsDataSource remote = new LodgingsRemoteDataSource();
            LodgingsDataSource repository = new LodgingsRepository(local, remote);
            repository.getLodgingsFavorite(new LodgingsDataSource.GetLodgingFavoriteCallback() {
                @Override
                public void onLoaded(List<ListingDetail> listingDetails) {
                    for (ListingDetail listingDetail : listingDetails) {
                        if (CalculateDistance.getDistance(latitude, longitude, listingDetail.getLat(), listingDetail.getLng())) {
                            listingDetailsAux.add(listingDetail);
                        }
                    }
                    if (listingDetailsAux.size() != 0) {
                        showNotification(listingDetailsAux);
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    Log.d(TAG, "onDataNotAvailable: ");
                }
            });
        }
    }

    private void showNotification(List<ListingDetail> listingDetails) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Airbnb")
                .setContentText("There are " + Integer.toString(listingDetails.size()) + " places near your position ")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        sendBroadcast(new Intent("UPDATE"));
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
