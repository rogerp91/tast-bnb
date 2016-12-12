package com.github.testairbnd.data.source.remote;

import android.support.annotation.NonNull;

import com.github.testairbnd.data.model.Detail;
import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.data.model.Lodging;
import com.github.testairbnd.data.source.LodgingsDataSource;
import com.github.testairbnd.util.Networks;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LodgingsRemoteDataSource implements LodgingsDataSource {

  @Inject
  HttpRestClient client;

  @Inject
  public LodgingsRemoteDataSource() {
  }

  @Override
  public void getLodgings(@NonNull String location, @NonNull final LoadLodgingCallback callback) {
    if (!Networks.isOnline(null)) {
      callback.onErrorNetwork();
      return;
    }
    Call<Lodging> call = client.getLodgings(HttpRestClient.TOKEN, location);
    call.enqueue(new Callback<Lodging>() {
      @Override
      public void onResponse(Call<Lodging> call, Response<Lodging> response) {
        if (!response.isSuccessful()) {
          callback.onErrorNotSolve();
        } else {
          callback.onLoaded(response.body());
        }
      }

      @Override
      public void onFailure(Call<Lodging> call, Throwable t) {
        callback.onErrorOcurred();
      }
    });

  }

  @Override
  public void refleshLodgings(@NonNull LoadLodgingCallback callback) {

  }

  @Override
  public void getLodgingsFavorite(@NonNull GetLodgingFavoriteCallback callback) {

  }

  @Override
  public void addLodgingsFavorite(@NonNull ListingDetail listingDetail, @NonNull AddLodgingFavoriteCallback callback) {

  }

  @Override
  public void removeLodgingsFavorite(int lodgingId, @NonNull RemoveLodgingFavoriteCallback callback) {

  }

  @Override
  public void getFavoriteSimpleForId(int lodgingId, @NonNull GetLodgingSimpleCallback callback) {

  }

  @Override
  public void getLodgingForId(int lodgingId, @NonNull final LoadForIdLodgingCallback callback) {
    if (!Networks.isOnline(null)) {
      callback.onErrorNetwork();
      return;
    }
    Call<Detail> call = client.getLodgingsForId(lodgingId, HttpRestClient.TOKEN, HttpRestClient.FORMA);
    call.enqueue(new Callback<Detail>() {
      @Override
      public void onResponse(Call<Detail> call, Response<Detail> response) {
        if (!response.isSuccessful()) {
          callback.onErrorNotSolve();
        } else {
          callback.onLoaded(response.body());
        }
      }

      @Override
      public void onFailure(Call<Detail> call, Throwable t) {
        callback.onErrorOcurred();
      }
    });
  }

  @Override
  public void deleteAllLodging() {

  }
}
