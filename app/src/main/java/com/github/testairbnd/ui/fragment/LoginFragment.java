package com.github.testairbnd.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.testairbnd.R;
import com.github.testairbnd.contract.LoginContract;
import com.github.testairbnd.data.model.DataIntent;
import com.github.testairbnd.data.model.Profile;
import com.github.testairbnd.ui.activity.MainActivity;
import com.github.testairbnd.util.Usefulness;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements LoginContract.View {

    private static final String TAG = LoginFragment.class.getSimpleName();

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private CallbackManager callbackManager;

    @BindView(R.id.layout_message_play)
    RelativeLayout layout_message_play;
    @BindView(R.id.text_message_play)
    TextView text_message_play;


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

        // Facebook
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

    @Override
    public void showErrorNotPlayServices(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message_play.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message_play.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message_play.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void showButtonFacebook(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        loginButton.setVisibility(active ? View.VISIBLE : View.GONE);
        loginButton.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginButton.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setFragment(this);
        loginButton.setReadPermissions("public_profile");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String id = object.getString("id");
                            String name = object.getString("name");
                            presenter.onSuccess(new Profile(id, name));
                        } catch (JSONException e) {
                            presenter.onError(e.getMessage());
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
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

    @OnClick(R.id.text_try_again_play)
    void onClickRequiredPlay() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=es_419"));
        startActivity(browserIntent);
    }
}
