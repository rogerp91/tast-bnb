package com.github.testairbnd.contract;

import android.support.annotation.NonNull;

import com.facebook.Profile;

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

    void onSuccess(Profile profile);

    void onError(String error);

    void onCancel();

  }

}
