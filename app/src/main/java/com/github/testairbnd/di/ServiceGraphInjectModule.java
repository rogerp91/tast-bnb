package com.github.testairbnd.di;

import com.github.testairbnd.service.GeofenceService;

import dagger.Module;

/**
 * Created by AndrewX on 13/12/2016.
 */
@Module(injects = {
  GeofenceService.class,
}, complete = false, library = true)
public class ServiceGraphInjectModule {

}
