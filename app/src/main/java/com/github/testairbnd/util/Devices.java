package com.github.testairbnd.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.github.testairbnd.TestAirbnb;

import kr.co.namee.permissiongen.PermissionGen;


/**
 * Created by Roger Patiño on 08/07/2016.
 */

public class Devices {

  /**
   * @return @android.os.{@link Build}
   */
  public static boolean isMarshmallow() {
    return Build.VERSION.SDK_INT > 22;
  }

  /**
   * Check permission
   *
   * @param context    @{@link Context}
   * @param permission @{@link android.Manifest.permission}
   * @return @boolean
   */
  public static boolean hasPermission(Context context, String permission) {
    int res = 0;
    if (permission.isEmpty()) {
      throw new IllegalArgumentException("I need the permission name");
    }
    Context contextAux = (context == null) ? TestAirbnb.getContext() : context;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      res = contextAux.checkSelfPermission(permission);
    }
    return res == PackageManager.PERMISSION_GRANTED;
  }

  public static void isMarshmallowCheckCoarseFine(Activity activity) {
    if (isMarshmallow()) {
      PermissionGen.with(activity).addRequestCode(100)
        .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).request();
    }
  }

  public static boolean isMarshmallowPermissionCoarseFine() {
    if (Devices.isMarshmallow()) {
      if (Devices.hasPermission(null, "android.permission.ACCESS_FINE_LOCATION") || Devices.hasPermission(null, "android.permission.ACCESS_COARSE_LOCATION")) {
        return true;
      } else {
        return false;
      }
    }else{
      return true;
    }
  }
}
