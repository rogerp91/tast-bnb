package com.github.testairbnd.contract;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by roger on 08/12/16.
 */

public interface MainContact {

  /**
   * TODO: View
   * <p>
   * {@link com.github.testairbnd.ui.activity.MainActivity}
   */
  public interface View {

    void setToolbar();

    void setActionBarIcon();

    void showDialogGPS();

    Context getContext();

    void locationFail();

    void locationSuccess();
  }

  /**
   * TODO: Presenter
   * {@link com.github.testairbnd.presenter.MainPresenter}
   */
  public interface Presenter {

    void setView(@NonNull View view);

    void onResume();

    void init();

  }

}
