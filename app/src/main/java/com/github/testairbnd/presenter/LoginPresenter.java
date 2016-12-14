package com.github.testairbnd.presenter;

import android.support.annotation.NonNull;

import com.github.rogerp91.pref.SP;
import com.github.testairbnd.contract.LoginContract;
import com.github.testairbnd.util.PlayServices;

import javax.inject.Inject;

import static com.github.testairbnd.util.Constants.FACEBOOK_ID;
import static com.github.testairbnd.util.Constants.FACEBOOK_NAME;
import static com.github.testairbnd.util.Constants.SESSION;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jsuarez on 05/12/16.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    @Inject
    public LoginPresenter() {
    }

    @Override
    public void setView(@NonNull LoginContract.View view) {
        checkNotNull(view, "View not null!");
        this.view = view;
    }

    @Override
    public void onResume() {
        if (!PlayServices.isGooglePlayServicesAvailable(view.getContext())) {
            view.showButtonFacebook(true);
        } else {
            view.showButtonFacebook(true);
        }
    }

    @Override
    public void onSuccess(com.github.testairbnd.data.model.Profile profile) {
        SP.putBoolean(SESSION, true);
        SP.putString(FACEBOOK_NAME, profile.getName());
        SP.putString(FACEBOOK_ID, profile.getId());
        if (!view.isActive()) {
            return;
        }
        view.goSuccess();
    }

    @Override
    public void onError(String error) {
        if (!view.isActive()) {
            return;
        }
        view.showError(error);
    }

    @Override
    public void onCancel() {
        if (!view.isActive()) {
            return;
        }
        view.showCancelLogin();
    }
}
