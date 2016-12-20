package com.github.testairbnd;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.github.rogerp91.pref.SP;
import com.github.testairbnd.di.AppModules;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import dagger.ObjectGraph;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rogerp91 on 05/12/16.
 */
//@ReportsCrashes(
//        formUri = "https://collector.tracepot.com/df10d880",
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.error_many
//)
public class TestAirbnb extends Application {

    private final static String TAG = TestAirbnb.class.getSimpleName();

    // Dagger
    private ObjectGraph objectGraph;

    // App Global
    private static TestAirbnb instance;

    /**
     * @return Application
     */
    public static TestAirbnb getInstance() {
        return instance;
    }

    /**
     * @return Context
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//    ACRA.init(this);
        instance = this;

        //Preference Shared
        new SP.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true).build();

        // DB
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        // Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //Dagger
        initDependencyInjection();

        //Hash
        printKeyHash();
    }

    /**
     * @param modules {@link Object}
     * @return ObjectGraph
     */
    public ObjectGraph buildGraphWithAditionalModules(List<Object> modules) {
        if (modules == null) {
            throw new IllegalArgumentException("Null module, review your getModules() implementation");
        }
        return objectGraph.plus(modules.toArray());
    }

    /**
     * Inject to {@link Application}
     */
    public void initDependencyInjection() {
        objectGraph = ObjectGraph.create(new AppModules(this));
        objectGraph.inject(this);
        objectGraph.injectStatics();
    }

    /**
     * Hash
     */
    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.github.testairbnd", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG, "SHA: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
