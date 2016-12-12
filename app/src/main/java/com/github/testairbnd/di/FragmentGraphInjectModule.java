package com.github.testairbnd.di;


import com.github.testairbnd.ui.fragment.DetailFragment;
import com.github.testairbnd.ui.fragment.FavoriteFragment;
import com.github.testairbnd.ui.fragment.HomeFragment;
import com.github.testairbnd.ui.fragment.LoginFragment;
import com.github.testairbnd.ui.fragment.MapFragment;

import dagger.Module;

/**
 * Created by Roger Patiño on 22/12/2015.
 */
@Module(injects = {
        LoginFragment.class,
        HomeFragment.class,
        FavoriteFragment.class,
        MapFragment.class,
        DetailFragment.class


}, complete = false, library = true)
public class FragmentGraphInjectModule {
}
