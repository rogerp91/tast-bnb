package com.github.testairbnd.util;

import android.location.Location;

/**
 * Created by AndrewX on 13/12/2016.
 */

public class CalculateDistance {

  public static boolean getDistance(Double centerLatitude, Double centerLongitude, double lat_local, double lon_local) {
    float[] results = new float[1];
    Location.distanceBetween(centerLatitude, centerLongitude, lat_local, lon_local, results);
    float distanceInMeters = results[0];
    int KM = 14000;
    return distanceInMeters < KM;
  }


}
