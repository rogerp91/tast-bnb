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

public class TestAirbnb extends Application {

  private ObjectGraph objectGraph;

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
    instance = this;

    new SP.Builder()
      .setContext(this)
      .setMode(ContextWrapper.MODE_PRIVATE)
      .setPrefsName(getPackageName())
      .setUseDefaultSharedPreference(true).build();

    Realm.init(this);
    RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
    Realm.setDefaultConfiguration(config);

    FacebookSdk.sdkInitialize(getApplicationContext());
    AppEventsLogger.activateApp(this);
    initDependencyInjection();
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
   * Inject
   */
  public void initDependencyInjection() {
    objectGraph = ObjectGraph.create(new AppModules(this));
    objectGraph.inject(this);
    objectGraph.injectStatics();
  }

  public void printKeyHash() {
    try {
      PackageInfo info = getPackageManager().getPackageInfo("com.github.testairbnd", PackageManager.GET_SIGNATURES);
      for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("TestAirbnb SHA: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
      }
    } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
}
