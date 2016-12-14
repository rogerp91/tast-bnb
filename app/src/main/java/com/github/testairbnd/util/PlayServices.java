package com.github.testairbnd.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by roger on 14/12/16.
 */

public class PlayServices {

    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1;

    public static void isGooglePlayServicesAvailable(Context context, Activity activity, View view) {
        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (GoogleApiAvailability.getInstance().isUserResolvableError(statusCode)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, statusCode, REQUEST_GOOGLE_PLAY_SERVICES);
            dialog.show();
        } else if (statusCode != ConnectionResult.SUCCESS) {
            Usefulness.showMessage(view, "Google Play Services required to use the app", Snackbar.LENGTH_LONG);
        }
    }

}