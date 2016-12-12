package com.github.testairbnd;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by roger on 18/10/16.
 */

@Qualifier
@Retention(RUNTIME)
public @interface ForApplication {
}
