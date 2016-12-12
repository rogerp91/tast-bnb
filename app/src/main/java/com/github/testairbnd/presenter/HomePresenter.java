package com.github.testairbnd.presenter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.testairbnd.R;
import com.github.testairbnd.TestAirbnb;
import com.github.testairbnd.contract.HomeContract;
import com.github.testairbnd.data.model.Lodging;
import com.github.testairbnd.data.model.Result;
import com.github.testairbnd.data.source.LodgingsDataSource;
import com.github.testairbnd.util.CheckLocation;
import com.github.testairbnd.util.Devices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import static android.content.Context.LOCATION_SERVICE;
import static com.github.testairbnd.util.Constants.CA;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by roger on 08/12/16.
 */

public class HomePresenter implements HomeContract.Presenter {

  private final static String TAG = HomePresenter.class.getSimpleName();

  //View
  private HomeContract.View view;

  //Repository
  private LodgingsDataSource repository;

  private Context context = TestAirbnb.getContext();
  private boolean gps = true;

  @Inject
  public HomePresenter(@Named("repository") LodgingsDataSource repository) {
    this.repository = repository;
  }

  @Override
  public void setView(@NonNull HomeContract.View view) {
    checkNotNull(view, "View not null!");
    this.view = view;
  }

  @Override
  public void onResume() {
    if (CheckLocation.isActiveGPS((LocationManager) view.getContext().getSystemService(LOCATION_SERVICE))) {
      Log.d(TAG, "onResume: CheckLocation.isActiveGPS");
      notActiveGPS();
      return;
    }

    if (!Devices.isMarshmallowPermissionCoarseFine()) {
      permissionFailed();
    }
  }

  @Override
  public void showViewProgress() {
    view.showErrorNotSolve(false);
    view.showNoModels(false);
    view.showErrorOcurred(false);
    view.showActiveGPS(false);
    view.showPermission(false);
    view.showNoLocalitation(false);
    view.showProgress(true);
  }

  @Override
  public void notShowView() {
    view.showProgress(false);
    view.showErrorNotSolve(false);
    view.showNoModels(false);
    view.showErrorOcurred(false);
    view.showActiveGPS(false);
    view.showPermission(false);
    view.showNoLocalitation(false);
  }

  @Override
  public void notActiveGPS() {
    notShowView();
    view.showActiveGPS(true);
  }

  @Override
  public void permissionFailed() {
    notShowView();
    view.showPermission(true);
  }

  @Override
  public void localitationNoAvailable(boolean from) {
    if (from) {
      notShowView();
      view.showNoLocalitation(true);
    } else {
      view.setLoadingIndicator(false);
      view.showMessage(context.getResources().getString(R.string.no_location));
    }
  }

  @Override
  public void loadModels(boolean showLoadingUI) {

  }

  @Override
  public void deleteAllModels() {
    repository.deleteAllLodging();
  }

  @Override
  public boolean gpsEnabled() {
    return gps;
  }

  @Override
  public void findPosition(Location mLastLocation, boolean from) {
    if (mLastLocation != null) {
      Geocoder geocoder = new Geocoder(context, Locale.getDefault());
      try {
        List<Address> list = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
        if (list != null && list.size() > 0) {
          Address address = list.get(0);
          if (address.getLocality() == null) {
            localitationNoAvailable(from);
          } else {
            if (from) {
              onProgress((address.getAdminArea() != null) ? address.getLocality() + "," + address.getAdminArea() : address.getLocality());
            } else {
              onIndicator((address.getAdminArea() != null) ? address.getLocality() + "," + address.getAdminArea() : address.getLocality());
            }
          }
        }
      } catch (IOException e) {
        Log.e(TAG, "findPosition: " + e.getMessage());
        localitationNoAvailable(from);
      }
    } else {
      Log.d(TAG, "findPosition: 2");
      localitationNoAvailable(from);
    }
  }

  @Override
  public void failGetPosition() {
    onProgress(CA);
  }

  @Override
  public void addFavorite(Result result) {

  }

  @Override
  public void removeFavorite(int id) {

  }

  private void onProgress(String location) {
    repository.getLodgings(location, new LodgingsDataSource.LoadLodgingCallback() {
      @Override
      public void onLoaded(Lodging lodgings) {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        if (lodgings.getResult().isEmpty()) {
          view.showNoModels(true);
        } else {
          view.showModels(lodgings);
        }
      }

      @Override
      public void onDataNotAvailable() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showNoModels(true);
      }

      @Override
      public void onErrorOcurred() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showErrorOcurred(true);
      }

      @Override
      public void onErrorNotSolve() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showErrorNotSolve(true);
      }

      @Override
      public void onErrorNetwork() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showNetworkError(true);
      }
    });
  }

  private void onIndicator(String location) {
    repository.refleshLodgings(new LodgingsDataSource.LoadLodgingCallback() {
      @Override
      public void onLoaded(Lodging lodgings) {
        if (!view.isActive()) {
          return;
        }
        view.setLoadingIndicator(false);
        if (lodgings.getResult().isEmpty()) {
          view.showNoModels(true);
        } else {
          view.showModels(lodgings);
        }
      }

      @Override
      public void onDataNotAvailable() {
        showMessage(context.getString(R.string.no_data_available));
      }

      @Override
      public void onErrorOcurred() {
        showMessage(context.getString(R.string.error_occurred));
      }

      @Override
      public void onErrorNotSolve() {
        showMessage(context.getString(R.string.error_many));
      }

      @Override
      public void onErrorNetwork() {
        showMessage(context.getString(R.string.no_connection));
      }
    });
  }

  private void showMessage(String msg) {
    if (!view.isActive()) {
      return;
    }
    view.setLoadingIndicator(false);
    view.showMessage(msg);
  }

}
