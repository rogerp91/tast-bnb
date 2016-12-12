package com.github.testairbnd.presenter;

import android.support.annotation.NonNull;

import com.github.testairbnd.contract.MapContract;
import com.github.testairbnd.data.source.LodgingsDataSource;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by roger on 08/12/16.
 */

public class MapPresenter implements MapContract.Presenter {

  private MapContract.View view;
  private LodgingsDataSource repository;

  @Inject
  public MapPresenter(@Named("repository") LodgingsDataSource repository) {
    this.repository = repository;
  }

  @Override
  public void setView(@NonNull MapContract.View view) {
    checkNotNull(view, "View not null!");
    this.view = view;
  }

  @Override
  public void start() {

  }

  @Override
  public void loadModels(boolean showLoadingUI) {

  }

  @Override
  public void deleteAllModels() {

  }
}
