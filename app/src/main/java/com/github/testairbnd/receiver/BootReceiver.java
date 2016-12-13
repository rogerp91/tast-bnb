package com.github.testairbnd.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.testairbnd.service.ManagerService;

/**
 * Created by AndrewX on 13/12/2016.
 */

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
      ManagerService.actionStartLocation(context);
    }
  }
}
