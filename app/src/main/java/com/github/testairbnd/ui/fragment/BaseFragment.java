package com.github.testairbnd.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.testairbnd.TestAirbnb;
import com.github.testairbnd.di.FragmentModule;
import com.github.testairbnd.util.PlayServices;
import com.github.testairbnd.util.Usefulness;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

/**
 * Created by roger on 07/12/16.
 */

public abstract class BaseFragment extends Fragment {

    protected void injectView(View view) {
        ButterKnife.bind(this, view);
    }

    private ObjectGraph activityGraph;

    public ActionBar ab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        ab = appCompatActivity.getSupportActionBar();
        injectDependencies();
    }

    private void injectDependencies() {
        TestAirbnb app = (TestAirbnb) getActivity().getApplication();
        List<Object> activityScopeModules = new ArrayList<>();
        activityScopeModules.add(new FragmentModule(getActivity()));
        activityGraph = app.buildGraphWithAditionalModules(activityScopeModules);
        inject(this);
    }

    private void inject(Object entityToGetInjected) {
        activityGraph.inject(entityToGetInjected);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activityGraph = null;
    }

}
