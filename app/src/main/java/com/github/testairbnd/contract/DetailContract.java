package com.github.testairbnd.contract;

import android.support.annotation.NonNull;

/**
 * Created by roger on 09/12/16.
 */


public interface DetailContract {

  /**
   * TODO: View
   * <p>
   * {@link com.github.testairbnd.ui.activity.DetailActivity}
   */
  public interface View {

    void setToolbar();

    void setActionBarUp();

  }

  /**
   * TODO: Presenter
   * {@link com.github.testairbnd.presenter.DetailPresenter}
   */
  public interface Presenter {

    void setView(@NonNull View view);

  }

}
