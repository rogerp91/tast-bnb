package com.github.testairbnd.data.source.local;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.data.source.LodgingsDataSource;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @see com.github.testairbnd.data.source.LodgingsDataSource
 */
public class LodgingsLocalDataSource implements LodgingsDataSource {

  private final static String TAG = LodgingsLocalDataSource.class.getSimpleName();
  private Realm realm;

  @Inject
  public LodgingsLocalDataSource() {
    realm = Realm.getDefaultInstance();
  }

  @Override
  public void getLodgings(@NonNull String location, @NonNull LoadLodgingCallback callback) {

  }

  @Override
  public void refleshLodgings(@NonNull LoadLodgingCallback callback) {

  }

  @Override
  public void getLodgingsFavorite(@NonNull GetLodgingFavoriteCallback callback) {
    Log.d(TAG, "getLodgingsFavorite: ");
    RealmResults<ListingDetail> listingDetails = realm.where(ListingDetail.class).findAll();
    if (!listingDetails.isEmpty()) {
      Log.d(TAG, "getLodgingsFavorite: " + listingDetails.size());
      callback.onLoaded(listingDetails);
    } else {
      callback.onDataNotAvailable();
    }
  }

  @Override
  public void addLodgingsFavorite(@NonNull final ListingDetail listingDetail, @NonNull final AddLodgingFavoriteCallback callback) {
    Log.d(TAG, "addLodgingsFavorite: ");
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(listingDetail);
      }
    });
  }

  @Override
  public void removeLodgingsFavorite(int lodgingId, @NonNull final RemoveLodgingFavoriteCallback callback) {
    Log.d(TAG, "removeLodgingsFavorite: ");
    final ListingDetail listingDetails = realm.where(ListingDetail.class).equalTo("id", lodgingId).findFirst();
    if (listingDetails == null) {
      callback.onDataNotAvailable();
      return;
    }
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        listingDetails.deleteFromRealm();
      }
    });

  }

  @Override
  public void getFavoriteSimpleForId(int lodgingId, @NonNull GetLodgingSimpleCallback callback) {
    final ListingDetail listingDetails = realm.where(ListingDetail.class).equalTo("id", lodgingId).findFirst();
    if (listingDetails == null) {
      callback.onDataNotAvailable();
    } else {
      callback.onLoaded(listingDetails);
    }
  }

  @Override
  public void getLodgingForId(int lodgingId, @NonNull LoadForIdLodgingCallback callback) {

  }

  @Override
  public void deleteAllLodging() {

  }
}
