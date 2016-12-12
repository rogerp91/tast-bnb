package com.github.testairbnd.di;

import com.github.testairbnd.data.source.LodgingsDataSource;
import com.github.testairbnd.data.source.repository.LodgingsRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger Patiño on 11/07/2016.
 */
@Module(complete = false, library = true)
public class RepositoryModule {

    @Provides
    @Singleton
    @Named("repository")
    LodgingsDataSource provideRepositoryLodgings(LodgingsRepository repository) {
        return repository;
    }
}
