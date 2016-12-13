package com.github.testairbnd.presenter;

import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.github.testairbnd.contract.MainContact;
import com.github.testairbnd.service.GeofenceService;
import com.github.testairbnd.service.ManagerService;
import com.github.testairbnd.util.Devices;
import com.github.testairbnd.util.ServiceTools;

import javax.inject.Inject;

import static android.content.Context.LOCATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by roger on 08/12/16.
 */

public class MainPresenter implements MainContact.Presenter {

  private MainContact.View view;

  @Inject
  public MainPresenter() {
  }

  @Override
  public void setView(@NonNull MainContact.View view) {
    checkNotNull(view, "View not null!");
    this.view = view;
  }

  @Override
  public void onResume() {
    // is GPS?
    LocationManager locationManager = (LocationManager) view.getContext().getSystemService(LOCATION_SERVICE);

    // Permission?
    if (Devices.isMarshmallow()) {
      if (Devices.hasPermission(null, "android.permission.ACCESS_FINE_LOCATION") || Devices.hasPermission(null, "android.permission.ACCESS_COARSE_LOCATION")) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
          view.showDialogGPS();
        } else {
          if (!ServiceTools.isServiceRunning(GeofenceService.class.getSimpleName())) {
            ManagerService.actionStartLocation(getApplicationContext());
          }
        }
      } else {
        view.locationFail();
      }
    } else {
      if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        view.showDialogGPS();
      } else {
        if (!ServiceTools.isServiceRunning(GeofenceService.class.getSimpleName())) {
          ManagerService.actionStartLocation(getApplicationContext());
        }
      }
    }
  }

  @Override
  public void init() {
    view.setToolbar();
    view.setActionBarIcon();
  }
}
