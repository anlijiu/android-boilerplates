package com.anlijiu.example.di;



import com.anlijiu.example.utils.IntentFactory;
import com.anlijiu.example.data.DebugIntentFactory;
import com.anlijiu.example.data.di.qualifier.CaptureIntents;
import com.anlijiu.example.ui.ViewContainer;
import com.anlijiu.example.ui.debug.DebugViewContainer;
import com.f2prateek.rx.preferences2.Preference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = UiModule.class)
public final class DebugUiModule {

    @Provides
    @Singleton
    ViewContainer provideAppContainer(DebugViewContainer appContainer) {
        return appContainer;
    }


    @Provides
    @Singleton
    IntentFactory provideIntentFactory(
            @CaptureIntents Preference<Boolean> captureIntents) {
        return new DebugIntentFactory(IntentFactory.REAL, captureIntents);
    }
}
