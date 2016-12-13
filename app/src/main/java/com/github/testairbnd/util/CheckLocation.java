package com.github.testairbnd.util;

import android.location.LocationManager;

/**
 * Created by roger on 09/12/16.
 */

public class CheckLocation {

  /**
   *
   * @param locationManager {@link LocationManager}
   * @return boolean
	 */
    public static boolean isActiveGPS(LocationManager locationManager) {
        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
