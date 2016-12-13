package com.github.testairbnd.service;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Roger Patiño on 13/12/2016.
 */

public class ManagerService {

  public static final String ENABLE = "enable";

  public static final String ACTION = "action."; // Action to start
  public static final String ACTION_START = "START"; // Action to start
  public static final String ACTION_ALIVE = "ALIVE"; // Action to reconnect
  //services position
  public static final String ACTION_SETTING_GPS = ACTION + "setting.gps";
  public static final String ACTION_PERMISSION_GPS = ACTION + "permission.gps";

  public static final int LOCATION_DEFAULT_INTERVAL = 120 * 1000;
  public static final int LOCATION_DEFAULT_FASTEST_INTERVAL = 30 * 1000;

  public static void actionStartLocation(Context ctx) {
    ctx.startService(new Intent(ctx, GeofenceService.class).setAction(ManagerService.ACTION_START));
  }
  public static void actionStopLocation(Context ctx) {
    ctx.stopService(new Intent(ctx, GeofenceService.class).setAction(ManagerService.ACTION_ALIVE));
  }

}
