package com.github.testairbnd.presenter;

import android.support.annotation.NonNull;

import com.github.testairbnd.contract.DetailContract;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by roger on 09/12/16.
 */

public class DetailPresenter implements DetailContract.Presenter {

  @Inject
  public DetailPresenter() {
  }

  @Override
  public void setView(@NonNull DetailContract.View view) {
    checkNotNull(view, "View not null!");
    view.setToolbar();
    view.setActionBarUp();
  }

}
