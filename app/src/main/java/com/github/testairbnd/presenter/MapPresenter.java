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
import com.github.testairbnd.contract.MapContract;
import com.github.testairbnd.data.model.Locality;
import com.github.testairbnd.data.model.Lodging;
import com.github.testairbnd.data.source.LodgingsDataSource;
import com.github.testairbnd.util.CheckLocation;
import com.github.testairbnd.util.Devices;
import com.github.testairbnd.util.Mapper;

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

public class MapPresenter implements MapContract.Presenter {

  private final static String TAG = MapPresenter.class.getSimpleName();

  private MapContract.View view;
  /**
   * Repository
   * {@link com.github.testairbnd.data.source.repository.LodgingsRepository}
   */
  private LodgingsDataSource repository;

  /**
   * @see Context of {@link TestAirbnb}
   */
  private Context context = TestAirbnb.getContext();

  //Is Gps active?
  private boolean gps = true;

  /**
   * Cache
   */
  private List<Locality> localities = null;

  @Inject
  public MapPresenter(@Named("repository") LodgingsDataSource repository) {
    this.repository = repository;
  }

  @Override
  public void setView(@NonNull MapContract.View view) {
    checkNotNull(view, "View not null!");
    this.view = view;
  }

  @Override
  public boolean gpsEnabled() {
    return gps;
  }

  @Override
  public void onResume() {
    // GPS active?
    if (CheckLocation.isActiveGPS((LocationManager) view.getContext().getSystemService(LOCATION_SERVICE))) {
      Log.d(TAG, "onResume: CheckLocation.isActiveGPS");
      notActiveGPS();
      return;
    }

    // Is Marshmallow with permission
    if (!Devices.isMarshmallowPermissionCoarseFine()) {
      permissionFailed();
    }
  }

  @Override
  public void showViewProgress() {
    view.showMap(false);
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
    view.showMap(false);
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
  public void findPosition(Location mLastLocation, boolean from) {
    if (mLastLocation != null) {
      // Get geocoder
      Geocoder geocoder = new Geocoder(context, Locale.getDefault());
      try {
        // Select 1
        List<Address> list = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);

        if (list != null && list.size() > 0) {
          Address address = list.get(0);
          if (address.getLocality() == null) {
            // Found, call the Angels CA
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
        // Found, call the Angels CA
        localitationNoAvailable(from);
      }
    } else {
      // Found, call the Angels CA
      localitationNoAvailable(from);
    }
  }

  @Override
  public void failGetPosition() {
    // Found, call the Angels CA
    onProgress(CA);
  }

  private void onProgress(final String location) {
    repository.getLodgings(location, new LodgingsDataSource.LoadLodgingCallback() {
      @Override
      public void onLoaded(final Lodging lodgings) {
        if (!view.isActive()) {
          return;
        }
        if (lodgings.getResult().isEmpty()) {
          view.showNoModels(true);
        } else {

          localities = Mapper.getLocalityOfResult(lodgings.getResult());
          if (localities == null) {
            view.showProgress(false);
            view.showNoModels(true);
          } else {
            view.showProgress(false);
            view.showMap(true);
            view.showModels(localities);
          }
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
        view.showMap(true);
        if (lodgings.getResult().isEmpty()) {
          view.showNoModels(true);
        } else {
          List<Locality> localities = Mapper.getLocalityOfResult(lodgings.getResult());
          view.showModels(localities);
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

  /**
   * @param msg Message
   */
  private void showMessage(String msg) {
    if (!view.isActive()) {
      return;
    }
    view.setLoadingIndicator(false);
    view.showMessage(msg);
  }

}
