package com.anlijiu.example.di;



import android.support.v7.app.AppCompatActivity;

import com.anlijiu.example.presentation.main.MainActivity;
import com.anlijiu.example.di.scope.ActivityScope;
import com.anlijiu.example.presentation.simple.SimpleActivity;

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

    @ActivityScope
    @ContributesAndroidInjector()
    abstract SimpleActivity simpleActivityInjector();

    @Binds
    @ActivityScope
    @Named("Root")
    abstract AppCompatActivity appCompatActivity(MainActivity mainActivity);

}
