package com.github.testairbnd.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.testairbnd.TestAirbnb;
import com.github.testairbnd.contract.ItemDetailContract;
import com.github.testairbnd.data.model.Detail;
import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.data.source.LodgingsDataSource;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by AndrewX on 11/12/2016.
 */

public class ItemDetailPresenter implements ItemDetailContract.Presenter {

  private ItemDetailContract.View view;

  //Repository
  private LodgingsDataSource repository;
  private Context context = TestAirbnb.getContext();
  private ListingDetail ld;

  @Inject
  public ItemDetailPresenter(@Named("repository") LodgingsDataSource repository) {
    this.repository = repository;
  }

  public void setView(@NonNull ItemDetailContract.View view) {
    checkNotNull(view, "View not null!");
    this.view = view;
    this.view.setTitle();
  }

  @Override
  public void start(int id) {
    Log.d("ItemDetailPresenter", "start: " + id);
    loadModels(id);
  }

  @Override
  public void loadModels(int id) {
    progress(id);
  }

  private void progress(final int id) {
    view.showProgress(true);
    repository.getLodgingForId(id, new LodgingsDataSource.LoadForIdLodgingCallback() {

      @Override
      public void onLoaded(Detail detail) {
        if (!view.isActive()) {
          return;
        }

        if (detail == null) {
          view.showProgress(false);
          view.showNoModels(true);
          return;
        }


        Log.d("LOAD", "onLoaded: ");

        ld = detail.getListing();
        view.setMap(ld.getLat(), ld.getLng());
        view.setName(ld.getName());
        view.setBackground(ld.getPicture_url());
        view.setPropertyType(ld.getProperty_type());
        view.setRoomType(ld.getRoom_type());
        view.setCountQuest(Integer.toString(ld.getGuests_included()));
        view.setCountBed(Integer.toString(ld.getBedrooms()));
        view.setCountBest(Integer.toString(ld.getBedrooms()));
        view.setCountBath(Double.toString(ld.getBathrooms()));
        view.setDescription(ld.getDescription());

        view.setPlace(ld.getAddress());
        view.showContainerView(true);
        view.showBarLayout(true);
        view.showProgress(false);
        view.setPrice(ld.getPrice_native() + " " + ld.getNative_currency());
        if (!view.isFavorite()) {
          view.showFavorite(false);
        } else {
          view.showFavorite(true);
        }
      }

      @Override
      public void onDataNotAvailable() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showNoModels(true);
      }

      @Override
      public void onErrorOcurred() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showErrorOcurred(true);
      }

      @Override
      public void onErrorNotSolve() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showErrorNotSolve(true);
      }

      @Override
      public void onErrorNetwork() {
        if (!view.isActive()) {
          return;
        }
        view.showProgress(false);
        view.showNetworkError(true);
      }
    });
  }

  @Override
  public void addFavorite() {
    ld.setFavorite(true);
    repository.addLodgingsFavorite(ld, new LodgingsDataSource.AddLodgingFavoriteCallback() {
      @Override
      public void onSuccess() {
        if (view.isActive()) {
          return;
        }
        view.showMessage("Added as a favorite");
      }

      @Override
      public void onDataNotAvailable() {
        if (view.isActive()) {
          return;
        }
        view.showMessage("Accommodation not available");
      }

      @Override
      public void onError() {
        if (view.isActive()) {
          return;
        }
        view.showMessage("Error adding favorite");
      }
    });
  }

  @Override
  public void removeFavorite() {
    repository.removeLodgingsFavorite(ld.getId(), new LodgingsDataSource.RemoveLodgingFavoriteCallback() {
      @Override
      public void onSuccess() {
        if (view.isActive()) {
          return;
        }
        view.showMessage("Removed from favorite");
      }

      @Override
      public void onDataNotAvailable() {
        if (view.isActive()) {
          return;
        }
        view.showMessage("Accommodation not available");
      }

      @Override
      public void onError() {
        if (view.isActive()) {
          return;
        }
        view.showMessage("Error deleting favorite");
      }
    });
  }

}
