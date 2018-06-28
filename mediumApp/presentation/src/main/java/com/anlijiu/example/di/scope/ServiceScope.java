package com.anlijiu.example.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by anlijiu on 17-12-11.
 */

@Scope
@Retention(RUNTIME)
public @interface ServiceScope {
}
