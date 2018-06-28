package com.anlijiu.example.di;



import com.anlijiu.example.ui.ViewContainer;
import com.anlijiu.example.ui.ReleaseViewContainer;
import com.f2prateek.rx.preferences2.Preference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = UiModule.class)
public final class ReleaseUiModule {

    @Provides
    @Singleton
    ViewContainer provideAppContainer(ReleaseViewContainer appContainer) {
        return appContainer;
    }


}
