package com.github.testairbnd.data.model;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by roger on 07/12/16.
 */

public class DataIntent {

    private Activity activity = null;
    private Class class_ = null;
    private Bundle bundle = null;
    private boolean finish = false;
    private int flag = 0;
    private boolean anim = false;

    public DataIntent() {
    }

    public DataIntent(Activity activity, Class class_, boolean finish) {
        this.activity = activity;
        this.class_ = class_;
        this.finish = finish;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Class getClass_() {
        return class_;
    }

    public void setClass_(Class class_) {
        this.class_ = class_;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isAnim() {
        return anim;
    }

    public void setAnim(boolean anim) {
        this.anim = anim;
    }
}
