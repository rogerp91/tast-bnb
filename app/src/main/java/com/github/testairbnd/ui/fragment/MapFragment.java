package com.github.testairbnd.ui.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.roger91.mlprogress.MlProgress;
import com.github.testairbnd.R;
import com.github.testairbnd.contract.MapContract;
import com.github.testairbnd.data.model.Locality;
import com.github.testairbnd.util.Usefulness;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
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

import static com.facebook.GraphRequest.TAG;
import static com.github.testairbnd.R.id.map;
import static com.github.testairbnd.util.Constants.UPDATE_FASTEST_INTERVAL;
import static com.github.testairbnd.util.Constants.UPDATE_INTERVAL;

/**
 * Created by roger on 07/12/16.
 */

public class MapFragment extends BaseFragment implements MapContract.View,
  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener {

  public static MapFragment newInstance() {
    return new MapFragment();
  }

  @BindView(R.id.progress)
  MlProgress mProgressView;

  @BindView(R.id.layout_message)
  RelativeLayout layout_message;
  @BindView(R.id.text_message)
  TextView text_message;
  private SupportMapFragment mapFragment;
  private GoogleMap mMap;

  @Inject
  MapContract.Presenter presenter;

  private Location mLastLocation;
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;

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

    mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
    mapFragment.getMapAsync(this);
    if (mapFragment == null && getActivity() != null) {
      FragmentManager fragmentManager = getFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      mapFragment = SupportMapFragment.newInstance();
      fragmentTransaction.replace(map, mapFragment).commit();
    }

    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
      .addConnectionCallbacks(this)
      .addOnConnectionFailedListener(this)
      .addApi(LocationServices.API)
      .build();
    buildGoogleApiClient();
    createLocationRequest();
    buildLocationSettingsRequest();

  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.start();
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
  public void setLoadingIndicator(final boolean active) {

  }

  @Override
  public void showModels(List<Locality> localities) {
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    List<MarkerOptions> markerOptionses = new ArrayList<>();
    for (int x = 0; x < localities.size(); x++) {
      MarkerOptions marker = new MarkerOptions().position(new LatLng(localities.get(x).getLatitud(), localities.get(x).getLongitud())).title(Integer.toString(localities.get(x).getPrice()) + localities.get(x).getLocalized_currency());
      markerOptionses.add(marker);
      marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
      mMap.addMarker(marker);
      mMap.setMyLocationEnabled(true);
      //bounds = new LatLngBounds.Builder().include(new LatLng(cn.getLatitude(), cn.getLongitude())).build();
    }

    for (int i = 0; i < markerOptionses.size(); i++) {
      mMap.addMarker(markerOptionses.get(i));
      builder.include(markerOptionses.get(i).getPosition());
    }

    LatLngBounds bounds = builder.build();
    int padding = 40; // offset from edges of the map in pixels
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
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

  @OnClick(R.id.text_try_again)
  void onClickTryAgain() {//Retry
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    LatLng sydney = new LatLng(-34, 151);
    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    Log.d(TAG, "onConnected: ");
    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (mLastLocation != null) {
      Log.d(TAG, "onConnected: " + mLastLocation.getAltitude());
    }

  }

  @Override
  public void onConnectionSuspended(int i) {
    if (i == CAUSE_SERVICE_DISCONNECTED) {
      showMessage("Disconnected. Please re-connect.");
    } else if (i == CAUSE_NETWORK_LOST) {
      showMessage("Network lost. Please re-connect.");
    }
    mGoogleApiClient.connect();
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
  }

  private synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
      .addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
  }

  private void createLocationRequest() {
    mLocationRequest = new LocationRequest().setInterval(UPDATE_INTERVAL).setFastestInterval(UPDATE_FASTEST_INTERVAL)
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
  }

  private void buildLocationSettingsRequest() {
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
    builder.addLocationRequest(mLocationRequest).setAlwaysShow(true);
  }


}
