package com.github.testairbnd.executor;

import android.os.Handler;
import android.os.Looper;

import javax.inject.Inject;

/**
 * Created by Roger Patiño on 30/11/2015.
 */
public class MainThreadImpl implements MainThread {

    private Handler handler;

    @Inject
    public MainThreadImpl() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable task) {
        handler.post(task);
    }
}
