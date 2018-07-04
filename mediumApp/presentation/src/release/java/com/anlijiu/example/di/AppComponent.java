package com.anlijiu.example.di;


import com.anlijiu.example.SampleApplication;
import com.anlijiu.example.data.di.ReleaseApiModule;
import com.anlijiu.example.data.di.ReleaseDataModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AppModule.class,
        ReleaseDataModule.class,
        ReleaseApiModule.class,
        ReleaseUiModule.class,
        ReleaseActivityBuildersModule.class,
        AndroidSupportInjectionModule.class,
        AndroidViewInjectionModule.class

})
interface AppComponent extends AndroidInjector<SampleApplication>{

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<SampleApplication> {}
}
