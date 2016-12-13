package com.github.testairbnd.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.testairbnd.data.model.DataIntent;
import com.github.testairbnd.util.AccessTokenFacebook;
import com.github.testairbnd.util.Usefulness;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    DataIntent dataIntent = new DataIntent();
    dataIntent.setActivity(this);
    dataIntent.setAnim(true);
    dataIntent.setFinish(true);
    if (!AccessTokenFacebook.isLoggedIn()) {
      dataIntent.setClass_(LoginActivity.class);// Change to LoginActivity.class
    }else{
      dataIntent.setClass_(MainActivity.class);
    }
    Usefulness.gotoActivity(dataIntent);
    super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
  }
}
