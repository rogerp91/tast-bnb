package com.github.testairbnd.di;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger Patiño on 01/12/2015.
 */
@Module(
        includes = {
                ActivityGraphModule.class,
                PresenterModule.class,
                RepositoryModule.class,
                DataSourceModule.class,

        },
        library = true,
        complete = false)
public class ActivityModule {

    private Activity activityContext;

    public ActivityModule(Activity activityContext) {
        this.activityContext = activityContext;
    }

    @Provides
    Context provideActivityContext() {
        return activityContext;
    }

    @Provides
    Activity provideActivityActivity() {//para el fragment
        return activityContext;
    }
}
