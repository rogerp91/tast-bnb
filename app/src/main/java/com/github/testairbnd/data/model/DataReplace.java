package com.github.testairbnd.data.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by roger on 07/12/16.
 */

public class DataReplace {

    private FragmentManager fragmentManager = null;
    private Fragment fragment = null;
    private Bundle bundle = null;
    private boolean anim = false;
    private int container = 0;

    public DataReplace(FragmentManager fragmentManager, Fragment fragment, int container) {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.container = container;
    }

    public int getContainer() {
        return container;
    }

    public void setContainer(int container) {
        this.container = container;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public boolean isAnim() {
        return anim;
    }

    public void setAnim(boolean anim) {
        this.anim = anim;
    }
}
