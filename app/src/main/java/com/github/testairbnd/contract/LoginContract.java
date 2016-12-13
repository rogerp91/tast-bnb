package com.github.testairbnd.contract;

import android.support.annotation.NonNull;

/**
 * Created by jsuarez on 05/12/16.
 */

public interface LoginContract {

  /**
   * TODO: View
   * <p>
   * {@link com.github.testairbnd.ui.fragment.LoginFragment}
   */
  public interface View {

    void goSuccess();

    void showCancelLogin();

    void showError(String error);

    boolean isActive();

  }


  /**
   * TODO: Presenter
   * {@link com.github.testairbnd.presenter.LoginPresenter}
   */
  public interface Presenter {

    void setView(@NonNull View view);

    void onSuccess(com.github.testairbnd.data.model.Profile profile);

    void onError(String error);

    void onCancel();

  }

}
