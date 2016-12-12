package com.github.testairbnd.data.source.repository;

import android.support.annotation.NonNull;

import com.github.testairbnd.data.model.Detail;
import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.data.model.Lodging;
import com.github.testairbnd.data.source.LodgingsDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class LodgingsRepository implements LodgingsDataSource {

  private LodgingsDataSource local;
  private LodgingsDataSource remote;

  @Inject
  LodgingsRepository(@Named("local") LodgingsDataSource local, @Named("remote") LodgingsDataSource remote) {
    this.local = local;
    this.remote = remote;
  }

  @Override
  public void getLodgings(@NonNull String location, @NonNull final LoadLodgingCallback callback) {
    remote.getLodgings(location, new LoadLodgingCallback() {
      @Override
      public void onLoaded(Lodging lodgings) {
        callback.onLoaded(lodgings);
      }

      @Override
      public void onErrorOcurred() {
        callback.onErrorOcurred();
      }

      @Override
      public void onErrorNotSolve() {
        callback.onErrorNotSolve();
      }

      @Override
      public void onErrorNetwork() {
        callback.onErrorNetwork();
      }

      @Override
      public void onDataNotAvailable() {
        callback.onDataNotAvailable();
      }
    });
  }

  @Override
  public void refleshLodgings(@NonNull LoadLodgingCallback callback) {
    getLodgings("", callback);
  }

  @Override
  public void getLodgingsFavorite(@NonNull final GetLodgingFavoriteCallback callback) {
    local.getLodgingsFavorite(new GetLodgingFavoriteCallback() {

      @Override
      public void onLoaded(List<ListingDetail> lodgings) {
        callback.onLoaded(lodgings);
      }

      @Override
      public void onDataNotAvailable() {
        callback.onDataNotAvailable();
      }
    });
  }

  @Override
  public void addLodgingsFavorite(@NonNull ListingDetail listingDetail, @NonNull AddLodgingFavoriteCallback callback) {
    local.addLodgingsFavorite(listingDetail, null);
  }

  @Override
  public void removeLodgingsFavorite(int lodgingId, @NonNull RemoveLodgingFavoriteCallback callback) {
    local.removeLodgingsFavorite(lodgingId, new RemoveLodgingFavoriteCallback() {
      @Override
      public void onSuccess() {

      }

      @Override
      public void onDataNotAvailable() {

      }

      @Override
      public void onError() {

      }
    });
  }

  @Override
  public void getFavoriteSimpleForId(int lodgingId, @NonNull final GetLodgingSimpleCallback callback) {
    local.getFavoriteSimpleForId(lodgingId, new GetLodgingSimpleCallback() {
      @Override
      public void onLoaded(ListingDetail listingDetail) {
        callback.onLoaded(listingDetail);
      }

      @Override
      public void onDataNotAvailable() {
        callback.onDataNotAvailable();
      }
    });
  }

  @Override
  public void getLodgingForId(final int lodgingId, @NonNull final LoadForIdLodgingCallback callback) {
    remote.getLodgingForId(lodgingId, new LoadForIdLodgingCallback() {

      @Override
      public void onErrorOcurred() {
        callback.onErrorOcurred();
      }

      @Override
      public void onErrorNotSolve() {
        callback.onErrorNotSolve();
      }

      @Override
      public void onErrorNetwork() {
        callback.onErrorNetwork();
      }

      @Override
      public void onLoaded(Detail detail) {
        callback.onLoaded(detail);
      }

      @Override
      public void onDataNotAvailable() {
        callback.onDataNotAvailable();
      }
    });
  }

  @Override
  public void deleteAllLodging() {
    local.deleteAllLodging();
  }

}
