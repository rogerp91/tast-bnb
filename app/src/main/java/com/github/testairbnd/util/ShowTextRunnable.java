package com.github.testairbnd.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Roger Patiño on 13/12/2016.
 */

/**
 *
 */
public class ShowTextRunnable implements Runnable {

  private String TAG = ShowTextRunnable.class.getSimpleName();
  private String mText;
  private Context context;

  public ShowTextRunnable(String mText, Context context) {
    this.mText = mText;
    this.context = context;
  }

  @Override
  public void run() {
    Toast.makeText(context, mText, Toast.LENGTH_LONG).show();
  }
}
