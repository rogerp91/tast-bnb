package com.github.testairbnd.di;

import com.github.testairbnd.data.source.LodgingsDataSource;
import com.github.testairbnd.data.source.local.LodgingsLocalDataSource;
import com.github.testairbnd.data.source.remote.LodgingsRemoteDataSource;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger Patiño on 11/07/2016.
 */
@Module(complete = false, library = true)
public class DataSourceModule {

  @Provides
  @Singleton
  @Named("local")
  LodgingsDataSource provideLocalDataSourceLodgings(LodgingsLocalDataSource local) {
    return local;
  }

  @Provides
  @Singleton
  @Named("remote")
  LodgingsDataSource provideRemoteDataSourceLodgings(LodgingsRemoteDataSource remote) {
    return remote;
  }

}
