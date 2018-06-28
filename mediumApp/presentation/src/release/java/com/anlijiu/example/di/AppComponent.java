package com.anlijiu.example.di;


import com.anlijiu.example.LauncherApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AppModule.class,
        ReleaseDataModule.class,
        ReleaseUiModule.class,
        ReleaseActivityBuildersModule.class,
        AndroidSupportInjectionModule.class,
//        PhoneModule.class,
        AndroidViewInjectionModule.class,

})
interface AppComponent extends AndroidInjector<LauncherApplication>{

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<LauncherApplication> {}
}
