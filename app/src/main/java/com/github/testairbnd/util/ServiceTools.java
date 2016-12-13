package com.github.testairbnd.util;

import android.app.ActivityManager;
import android.content.Context;

import com.github.testairbnd.TestAirbnb;

import java.util.List;

/**
 * Created by AndrewX on 13/12/2016.
 */

public class ServiceTools {

  /**
   * {@link android.app.Service} running
   *
   * @param serviceClassName {@link Class}
   * @return is running
   */
  public static boolean isServiceRunning(String serviceClassName) {
    final ActivityManager activityManager = (ActivityManager) TestAirbnb.getContext().getSystemService(Context.ACTIVITY_SERVICE);
    final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
    for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
      if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
        return true;
      }
    }
    return false;
  }

}
