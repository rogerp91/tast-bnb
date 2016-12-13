package com.github.testairbnd.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.testairbnd.TestAirbnb;

/**
 * Created by Roger Patiño on 15/06/2016.
 */
public class Networks {

    /**
     * Connexion
     *
     * @param context {@link Context}
     * @return boolean @{@link Boolean}
     */
    public static boolean isOnline(Context context) {
        Context ctx = context == null ? TestAirbnb.getContext() : context;
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
