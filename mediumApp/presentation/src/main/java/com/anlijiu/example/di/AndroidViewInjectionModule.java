package com.anlijiu.example.di;

/**
 * Created by anlijiu on 17-11-22.
 */

import android.view.View;

import java.util.Map;

import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.Multibinds;

@Module
public abstract class AndroidViewInjectionModule {
    @Multibinds
    abstract Map<Class<? extends View>, AndroidInjector.Factory<? extends View>>
    viewInjectorFactories();
}
