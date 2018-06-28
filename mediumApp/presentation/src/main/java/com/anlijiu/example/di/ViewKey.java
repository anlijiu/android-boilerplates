package com.anlijiu.example.di;

import android.view.View;

import java.lang.annotation.Target;

import dagger.MapKey;
import dagger.internal.Beta;

import static java.lang.annotation.ElementType.METHOD;


/**
 * Created by anlijiu on 17-11-22.
 */


@Beta
@MapKey
@Target(METHOD)
public @interface ViewKey {
    Class<? extends View> value();
}