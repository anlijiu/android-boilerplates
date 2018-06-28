package com.anlijiu.example.di;


import com.anlijiu.example.SampleApplication;
import com.anlijiu.example.data.di.DebugDataModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AppModule.class,
        DebugDataModule.class,
        DebugUiModule.class,
        DebugActivityBuildersModule.class,
        DebugViewModule.class,
        AndroidSupportInjectionModule.class,
        AndroidViewInjectionModule.class

})
interface AppComponent extends AndroidInjector<SampleApplication>{

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<SampleApplication> {}
}
