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
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.roger91.mlprogress.MlProgress;
import com.github.testairbnd.R;
import com.github.testairbnd.contract.HomeContract;
import com.github.testairbnd.data.model.BundleDetail;
import com.github.testairbnd.data.model.DataIntent;
import com.github.testairbnd.data.model.Lodging;
import com.github.testairbnd.data.model.Result;
import com.github.testairbnd.ui.activity.DetailActivity;
import com.github.testairbnd.ui.adapte.LodgingsAdapte;
import com.github.testairbnd.util.OnItemClickAddLodgings;
import com.github.testairbnd.util.Usefulness;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.testairbnd.util.Constants.INTENT_ACTION_NOT_GPS_ACTIVE;
import static com.github.testairbnd.util.Constants.INTENT_ACTION_PERMISSION_FAILED;
import static com.github.testairbnd.util.Constants.INTENT_ACTION_PERMISSION_SUCCESS;
import static com.github.testairbnd.util.Constants.UPDATE_FASTEST_INTERVAL;

/**
 * Created by roger on 07/12/16.
 */

public class HomeFragment extends BaseFragment implements
  HomeContract.View,
  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  private final static String TAG = HomeFragment.class.getSimpleName();

  public static HomeFragment newInstance() {
    return new HomeFragment();
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


  @BindView(R.id.recycler_list)
  RecyclerView recycler;
  @BindView(R.id.swipe_refresh)
  SwipeRefreshLayout swipeRefreshLayout;

  @Inject
  HomeContract.Presenter presenter;

  private LodgingsAdapte adapte;

  private Location mLastLocation;
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;

  private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
  private List<Result> resultsAux;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_generic, container, false);
    injectView(view);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    //Recycler
    recycler.setHasFixedSize(true);
    LinearLayoutManager linearManager = new LinearLayoutManager(getActivity());
    recycler.setLayoutManager(linearManager);

    //Presenter
    presenter.setView(this);

    //Refresh
    swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary),
      ContextCompat.getColor(getActivity(), R.color.colorAccent),
      ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
    );
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        presenter.findPosition(mLastLocation, false);
      }
    });

    // Localization
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
  public void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
    try {
      adapte.clearData();
    } catch (NullPointerException e) {
      Log.e(TAG, "onStart: " + e.getMessage());
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    // Show View Progress
    presenter.showViewProgress();

    // Recycler, problem to load data
    recycler.setItemViewCacheSize(0);

    // Check
    presenter.onResume();

    // Intent from MainActivity
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
  public void setTitle() {
//    ab.setTitle("Home");
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
    if (getView() == null) {
      return;
    }
    final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
    srl.post(new Runnable() {
      @Override
      public void run() {
        srl.setRefreshing(active);
      }
    });
  }

  @Override
  public void showModels(Lodging objects) {
    adapte = new LodgingsAdapte(objects.getResult(), onClickAddFavorite);
    adapte.setHasStableIds(true);
    recycler.setAdapter(adapte);
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

    presenter.findPosition(mLastLocation, true);
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

  /**
   * {@link LodgingsAdapte}
   */
  public OnItemClickAddLodgings onClickAddFavorite = new OnItemClickAddLodgings() {

    @Override
    public void gotoDetails(BundleDetail bundleDetail) {
      Bundle bundle = new Bundle();
      bundle.putBoolean("favorite", true);
      bundle.putParcelable("detail", bundleDetail);
      DataIntent dataIntent = new DataIntent();
      dataIntent.setActivity(getActivity());
      dataIntent.setClass_(DetailActivity.class);
      dataIntent.setBundle(bundle);
      Usefulness.gotoActivity(dataIntent);
    }
  };

  @OnClick(R.id.text_try_again)
  void onClickTryAgain() {//Retry
    presenter.showViewProgress();//Always view
    presenter.findPosition(mLastLocation, true);
  }

  @OnClick(R.id.text_try_again_gps)
  void onClickTryAgainGPS() { //Retry
    presenter.showViewProgress();//Always view
    presenter.findPosition(mLastLocation, true);
  }

  @OnClick(R.id.btn_required_gps)
  void onClickRequiredGPS() {
    getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
  }

  @OnClick(R.id.text_try_again_per)
  void onClickTryAgainPer() {//Retry
    presenter.showViewProgress();//Always view
    presenter.findPosition(mLastLocation, true);
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
    presenter.showViewProgress();//Always view
    presenter.findPosition(mLastLocation, true);
  }

  @OnClick(R.id.btn_required_loc)
  void onClickRequiredLoc() {
    presenter.showViewProgress(); //Always view
    presenter.failGetPosition();
  }
}
