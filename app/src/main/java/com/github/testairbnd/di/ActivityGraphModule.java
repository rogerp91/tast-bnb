package com.github.testairbnd.di;

import com.github.testairbnd.ui.activity.DetailActivity;
import com.github.testairbnd.ui.activity.MainActivity;

import dagger.Module;

@Module(
        injects = {
                MainActivity.class,
                DetailActivity.class

        },
        complete = false
)
class ActivityGraphModule {

}
