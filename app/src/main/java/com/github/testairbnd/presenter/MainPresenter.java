package com.github.testairbnd.presenter;

import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.github.testairbnd.contract.MainContact;
import com.github.testairbnd.util.Devices;

import javax.inject.Inject;

import static android.content.Context.LOCATION_SERVICE;
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
        LocationManager locationManager = (LocationManager) view.getContext().getSystemService(LOCATION_SERVICE);
        if (Devices.isMarshmallow()) {
            if (Devices.hasPermission(null, "android.permission.ACCESS_FINE_LOCATION") || Devices.hasPermission(null, "android.permission.ACCESS_COARSE_LOCATION")) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    view.showDialogGPS();
                }
            } else {
                view.locationFail();
            }
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                view.showDialogGPS();
            }
        }
    }

    @Override
    public void init() {
        view.setToolbar();
        view.setActionBarIcon();
    }
}
