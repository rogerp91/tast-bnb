package com.github.testairbnd.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.rogerp91.pref.SP;
import com.github.testairbnd.data.model.DataIntent;
import com.github.testairbnd.util.Usefulness;

import static com.github.testairbnd.util.Constants.SESSION;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    boolean session = SP.getBoolean(SESSION, false);
    DataIntent dataIntent = new DataIntent();
    dataIntent.setActivity(this);
    dataIntent.setAnim(true);
    dataIntent.setFinish(true);
    if (!session) {
      dataIntent.setClass_(MainActivity.class);
    } else {
      dataIntent.setClass_(MainActivity.class);
    }
    Usefulness.gotoActivity(dataIntent);
    super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
  }
}
