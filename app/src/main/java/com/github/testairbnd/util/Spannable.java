package com.github.testairbnd.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by AndrewX on 11/12/2016.
 */

public class Spannable extends ClickableSpan {

  private boolean isUnderline = true;

  /**
   * Constructor
   */
  public Spannable(boolean isUnderline) {
    this.isUnderline = isUnderline;
  }

  @Override
  public void updateDrawState(TextPaint ds) {
    ds.setUnderlineText(isUnderline);
  }

  @Override
  public void onClick(View widget) {

  }
}
