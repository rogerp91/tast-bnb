package com.github.testairbnd.presenter;

import android.support.annotation.NonNull;

import com.facebook.Profile;
import com.github.rogerp91.pref.SP;
import com.github.testairbnd.contract.LoginContract;

import javax.inject.Inject;

import static com.github.testairbnd.util.Constants.FACEBOOK_FIRST;
import static com.github.testairbnd.util.Constants.FACEBOOK_ID;
import static com.github.testairbnd.util.Constants.FACEBOOK_LAST;
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
    public void onSuccess(Profile profile) {
        SP.putBoolean(SESSION, true);
        SP.putString(FACEBOOK_FIRST, profile.getFirstName());
        SP.putString(FACEBOOK_LAST, profile.getLastName());
        SP.putString(FACEBOOK_ID, profile.getLastName());
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
