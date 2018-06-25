package com.example.anlijiu.library.di;



import android.support.v7.app.AppCompatActivity;

import com.example.anlijiu.library.MainActivity;
import com.example.anlijiu.library.di.scope.ActivityScope;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ActivityBuildersModule {
    @ActivityScope
    @ContributesAndroidInjector()
    abstract MainActivity mainActivityInjector();

    @Binds
    @ActivityScope
    @Named("Root")
    abstract AppCompatActivity appCompatActivity(MainActivity mainActivity);

}
