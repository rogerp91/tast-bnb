package com.github.testairbnd.di;


import com.github.testairbnd.ForApplication;
import com.github.testairbnd.TestAirbnb;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger Patiño on 06/01/2016.
 */
@Module(
        injects = TestAirbnb.class,
        library = true,
        includes = {
                ExecutorModule.class
        }
)
public class AppModules {

    public TestAirbnb app;

    public AppModules(TestAirbnb app) {
        this.app = app;
    }

    @Singleton
    @Provides
    @ForApplication
    TestAirbnb provideApplication() {
        return this.app;
    }
}
