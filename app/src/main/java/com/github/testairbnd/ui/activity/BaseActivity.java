package com.github.testairbnd.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.testairbnd.TestAirbnb;
import com.github.testairbnd.di.ActivityModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

/**
 * Created by roger on 07/12/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected void injectView() {
        ButterKnife.bind(this);
    }

    private ObjectGraph activityGraph;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    private void injectDependencies() {
        TestAirbnb app = (TestAirbnb) getApplication();
        List<Object> activityScopeModules = new ArrayList<>();
        activityScopeModules.add(new ActivityModule(this));
        activityGraph = app.buildGraphWithAditionalModules(activityScopeModules);
        inject(this);
    }

    private void inject(Object entityToGetInjected) {
        activityGraph.inject(entityToGetInjected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityGraph = null;
    }
}
