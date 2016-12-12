package com.github.testairbnd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.testairbnd.R;
import com.github.testairbnd.contract.LoginContract;
import com.github.testairbnd.data.model.DataIntent;
import com.github.testairbnd.ui.activity.MainActivity;
import com.github.testairbnd.util.Usefulness;

import javax.inject.Inject;

import butterknife.BindView;

public class LoginFragment extends BaseFragment implements LoginContract.View {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private CallbackManager callbackManager;

    @BindView(R.id.login_button)
    LoginButton loginButton;

    @Inject
    LoginContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        injectView(view);
        // Presenter
        presenter.setView(this);
        init();
        return view;
    }

    @Override
    public void goSuccess() {
        DataIntent dataIntent = new DataIntent();
        dataIntent.setActivity(getActivity());
        dataIntent.setAnim(true);
        dataIntent.setFinish(true);
        dataIntent.setClass_(MainActivity.class);
        dataIntent.setFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Usefulness.gotoActivity(dataIntent);
    }

    @Override
    public void showCancelLogin() {
        Usefulness.showMessage(getView(), "Login has been canceled", Snackbar.LENGTH_LONG);
    }

    @Override
    public void showError(String error) {
        Usefulness.showMessage(getView(), error, Snackbar.LENGTH_LONG);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void init() {
        // Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton.setFragment(this);
//        loginButton.setReadPermissions("email");
//        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                presenter.onSuccess(Profile.getCurrentProfile());
            }

            @Override
            public void onCancel() {
                presenter.onCancel();
            }

            @Override
            public void onError(FacebookException exception) {
                presenter.onError(exception.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
