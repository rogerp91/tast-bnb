package com.github.testairbnd.contract;

import android.content.Context;
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

        void showErrorNotPlayServices(final boolean active);

        void showButtonFacebook(final boolean active);

        Context getContext();

    }


    /**
     * TODO: Presenter
     * {@link com.github.testairbnd.presenter.LoginPresenter}
     */
    public interface Presenter {

        void setView(@NonNull View view);

        void onResume();

        void onSuccess(com.github.testairbnd.data.model.Profile profile);

        void onError(String error);

        void onCancel();

    }

}
