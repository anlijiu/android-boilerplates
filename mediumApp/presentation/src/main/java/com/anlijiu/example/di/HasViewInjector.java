package com.anlijiu.example.di;


import android.app.Activity;
import android.view.View;

import dagger.android.AndroidInjector;


/**
 * Created by anlijiu on 17-11-22.
 */

public interface HasViewInjector<V> {

    /**
     * Returns an {@link AndroidInjector} of {@link Activity}s.
     */
    AndroidInjector<View> viewInjector();
}