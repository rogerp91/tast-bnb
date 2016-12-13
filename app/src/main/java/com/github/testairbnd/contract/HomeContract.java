package com.github.testairbnd.contract;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.github.testairbnd.data.model.Lodging;
import com.github.testairbnd.data.model.Result;

/**
 * Created by roger on 08/12/16.
 */

public interface HomeContract {

  /**
   * TODO: View
   * <p>
   * {@link com.github.testairbnd.ui.fragment.HomeFragment}
   */
  public interface View {

    void setTitle();

    void showProgress(boolean active);

    void setLoadingIndicator(boolean active);

    void showModels(Lodging lodging);

    void showNoModels(final boolean active);

    void showNetworkError(final boolean active);

    void showErrorOcurred(final boolean active);

    void showErrorNotSolve(final boolean active);

    void showMessage(String message);

    boolean isActive();

    void showActiveGPS(boolean active);

    void showPermission(boolean active);

    void showNoLocalitation(boolean active);

    Context getContext();

  }

  /**
   * TODO: Presenter
   * {@link com.github.testairbnd.presenter.HomePresenter}
   */
  public interface Presenter {

    void setView(@NonNull HomeContract.View view);

    void onResume();

    void showViewProgress();

    void notShowView();

    void notActiveGPS();

    void permissionFailed();

    void localitationNoAvailable(boolean from);

    void loadModels(boolean showLoadingUI);

    void deleteAllModels();

    boolean gpsEnabled();

    void findPosition(Location mLastLocation, boolean from);

    void failGetPosition();

    void addFavorite(Result result);

    void removeFavorite(int id);

  }

}
