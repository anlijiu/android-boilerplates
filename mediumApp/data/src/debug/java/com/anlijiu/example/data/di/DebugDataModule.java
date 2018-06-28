package com.anlijiu.example.data.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.anlijiu.example.data.entity.MyObjectBox;
import com.anlijiu.example.data.di.qualifier.AnimationSpeed;
import com.anlijiu.example.data.di.qualifier.CaptureIntents;
import com.anlijiu.example.data.di.qualifier.IsMockMode;
import com.anlijiu.example.data.di.qualifier.PixelGridEnabled;
import com.anlijiu.example.data.di.qualifier.PixelRatioEnabled;
import com.anlijiu.example.data.di.qualifier.ScalpelEnabled;
import com.anlijiu.example.data.di.qualifier.ScalpelWireframeEnabled;
import com.anlijiu.example.data.di.qualifier.SeenDebugDrawer;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

@Module(includes = {DataModule.class})
public final class DebugDataModule {
    private static final int DEFAULT_ANIMATION_SPEED = 1; // 1x (normal) speed.
    private static final boolean DEFAULT_PIXEL_GRID_ENABLED = false; // No pixel grid overlay.
    private static final boolean DEFAULT_PIXEL_RATIO_ENABLED = false; // No pixel ratio overlay.
    private static final boolean DEFAULT_SCALPEL_ENABLED = false; // No crazy 3D view tree.
    private static final boolean DEFAULT_SCALPEL_WIREFRAME_ENABLED = false; // Draw views by
    private static final boolean DEFAULT_SEEN_DEBUG_DRAWER = false; // Show debug drawer first time.
    private static final boolean DEFAULT_CAPTURE_INTENTS = true; // Capture external intents.

    @Provides
    @Singleton
    BoxStore provideMyObjectBox(Application application) {
        BoxStore boxStore = MyObjectBox.builder().androidContext(application).build();
        new AndroidObjectBrowser(boxStore).start(application);
        return boxStore;
    }

    @Provides
    @Singleton
    AndroidObjectBrowser provideAndroidObjectBrowser(BoxStore boxStore) {
        return new AndroidObjectBrowser(boxStore);
    }

    @Provides
    @Singleton
    @CaptureIntents
    Preference<Boolean> provideCaptureIntentsPreference(RxSharedPreferences prefs) {
        return prefs.getBoolean("debug_capture_intents", DEFAULT_CAPTURE_INTENTS);
    }

    @Provides
    @Singleton
    @AnimationSpeed
    Preference<Integer> provideAnimationSpeed(RxSharedPreferences prefs) {
        return prefs.getInteger("debug_animation_speed", DEFAULT_ANIMATION_SPEED);
    }

    @Provides
    @Singleton
    @PixelGridEnabled
    Preference<Boolean> providePixelGridEnabled(RxSharedPreferences prefs) {
        return prefs.getBoolean("debug_pixel_grid_enabled", DEFAULT_PIXEL_GRID_ENABLED);
    }

    @Provides
    @Singleton
    @PixelRatioEnabled
    Preference<Boolean> providePixelRatioEnabled(RxSharedPreferences prefs) {
        return prefs.getBoolean("debug_pixel_ratio_enabled", DEFAULT_PIXEL_RATIO_ENABLED);
    }

    @Provides
    @Singleton
    @SeenDebugDrawer
    Preference<Boolean> provideSeenDebugDrawer(RxSharedPreferences prefs) {
        return prefs.getBoolean("debug_seen_debug_drawer", DEFAULT_SEEN_DEBUG_DRAWER);
    }

    @Provides
    @Singleton
    @ScalpelEnabled
    Preference<Boolean> provideScalpelEnabled(RxSharedPreferences prefs) {
        return prefs.getBoolean("debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
    }

    @Provides
    @Singleton
    @ScalpelWireframeEnabled
    Preference<Boolean> provideScalpelWireframeEnabled(RxSharedPreferences prefs) {
        return prefs.getBoolean("debug_scalpel_wireframe_drawer", DEFAULT_SCALPEL_WIREFRAME_ENABLED);
    }


}
