package com.github.testairbnd.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by benjakuben on 12/17/14.
 */
public class LocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

  public interface LocationCallback {
    void handleNewLocation(Location location);
  }

  public static final String TAG = LocationProvider.class.getSimpleName();
  private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 0;
  private LocationCallback mLocationCallback;
  private Context context;
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;

  public LocationProvider(LocationCallback callback, Context context) {
    this.context = context;
    mGoogleApiClient = new GoogleApiClient.Builder(context)
      .addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    mLocationCallback = callback;
    mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
  }

  public void connect() {
    Log.d(TAG, "connect: ");
    mGoogleApiClient.connect();
  }

  public void disconnect() {
    Log.d(TAG, "disconnect: ");
    if (mGoogleApiClient.isConnected()) {
      LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
      mGoogleApiClient.disconnect();
    }
  }

  @Override
  public void onConnected(Bundle bundle) {
    Log.d(TAG, "Loct services connected.");
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (location == null) {
      LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    } else {
      mLocationCallback.handleNewLocation(location);
    }
  }

  @Override
  public void onConnectionSuspended(int cause) {
    Log.i(TAG, "Connection suspended");
    mGoogleApiClient.connect();
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    if (connectionResult.hasResolution() && context instanceof Activity) {
      try {
        Activity activity = (Activity) context;
        connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
      } catch (IntentSender.SendIntentException e) {
        e.printStackTrace();
      }
    } else {
      Log.d(TAG, "Loct services connection failed with code " + connectionResult.getErrorCode());
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    mLocationCallback.handleNewLocation(location);
  }
}
