package com.github.testairbnd.di;


import com.github.testairbnd.executor.Executor;
import com.github.testairbnd.executor.MainThread;
import com.github.testairbnd.executor.MainThreadImpl;
import com.github.testairbnd.executor.ThreadExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger Patiño on 30/11/2015.
 */
@Module(library = true)
public class ExecutorModule {

    @Provides
    @Singleton
    Executor provideExecutor(ThreadExecutor threadExecutor) {
        return threadExecutor;
    }

    @Provides
    @Singleton
    MainThread provideMainThread(MainThreadImpl impl) {
        return impl;
    }
}
