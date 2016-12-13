package com.github.testairbnd.contract;

import android.support.annotation.NonNull;

/**
 * Created by roger on 09/12/16.
 */

public interface ItemDetailContract {

  /**
   * TODO: View
   * <p>
   * {@link com.github.testairbnd.ui.fragment.DetailFragment}
   */
  public interface View {

    void setTitle();

    void setBackground(String url);

    void setPrice(String price);

    void setName(String name);

    void setPropertyType(String propertyType);

    void setRoomType(String roomType);

    void setCountQuest(String countQuest);

    void setCountBed(String countBed);

    void setCountBest(String countBest);

    void setCountBath(String countBath);

    void setDescription(String description);

    void setMap(Double lat, Double lon);

    void setPlace(String place);

    void showProgress(boolean active);

    void showBarLayout(boolean active);

    void showNoModels(final boolean active);

    void showNetworkError(final boolean active);

    void showErrorOcurred(final boolean active);

    void showErrorNotSolve(final boolean active);

    void showContainerView(final boolean active);

    void showFavorite(final boolean active);

    boolean isActive();

    boolean isFavorite();

    void showMessage(String message);
  }

  /**
   * TODO: Presenter
   * {@link com.github.testairbnd.presenter.ItemDetailPresenter}
   */
  public interface Presenter {

    void setView(@NonNull ItemDetailContract.View view);

    void start(int id);

    void loadModels(int id);

    void addFavorite();

    void removeFavorite();

  }

}
