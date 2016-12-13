package com.github.testairbnd.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger Patiño on 13/12/2016.
 */

@Module(
  includes = {
    ServiceGraphInjectModule.class,
    RepositoryModule.class,
    DataSourceModule.class,
    ClientModule.class,
  },
  library = true,
  complete = false)
public class ServiceModule {

  private final Context context;

  public ServiceModule(Context context) {
    this.context = context;
  }

  @Provides
  Context provideContext() {
    return context;
  }
}
