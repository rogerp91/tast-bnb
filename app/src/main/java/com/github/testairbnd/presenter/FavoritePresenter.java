package com.github.testairbnd.presenter;

import android.support.annotation.NonNull;

import com.github.testairbnd.contract.FavoriteContract;
import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.data.source.LodgingsDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by roger on 08/12/16.
 */

public class FavoritePresenter implements FavoriteContract.Presenter {

  private FavoriteContract.View view;
  private LodgingsDataSource repository;

  @Inject
  public FavoritePresenter(@Named("repository") LodgingsDataSource repository) {
    this.repository = repository;
  }

  @Override
  public void setView(@NonNull FavoriteContract.View view) {
    checkNotNull(view, "View not null!");
    this.view = view;
    this.view.setTitle();
  }

  @Override
  public void start() {
    view.showProgress(true);
    repository.getLodgingsFavorite(new LodgingsDataSource.GetLodgingFavoriteCallback() {
      @Override
      public void onLoaded(final List<ListingDetail> listingDetails) {
        if (!view.isActive()) {
          return;
        }
        if (listingDetails.isEmpty()) {
          view.showProgress(false);
          view.showNoModels(true);
          return;
        }

        view.showModels(listingDetails);
        view.showProgress(false);
      }

      @Override
      public void onDataNotAvailable() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showNoModels(true);
      }
    });
  }

  @Override
  public void loadModels(boolean showLoadingUI) {

  }

  @Override
  public void deleteAllModels() {

  }
}
