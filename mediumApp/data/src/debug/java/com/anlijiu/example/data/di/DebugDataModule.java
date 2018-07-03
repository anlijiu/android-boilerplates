package com.anlijiu.example.data.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.anlijiu.example.data.cloud.ApiEndpoints;
import com.anlijiu.example.data.di.qualifier.ApiEndpoint;
import com.anlijiu.example.data.di.qualifier.NetworkDelay;
import com.anlijiu.example.data.di.qualifier.NetworkFailurePercent;
import com.anlijiu.example.data.di.qualifier.NetworkVariancePercent;
import com.anlijiu.example.data.entity.MyObjectBox;
import com.anlijiu.example.data.di.qualifier.AnimationSpeed;
import com.anlijiu.example.data.di.qualifier.CaptureIntents;
import com.anlijiu.example.data.di.qualifier.IsMockMode;
import com.anlijiu.example.data.di.qualifier.PixelGridEnabled;
import com.anlijiu.example.data.di.qualifier.PixelRatioEnabled;
import com.anlijiu.example.data.di.qualifier.ScalpelEnabled;
import com.anlijiu.example.data.di.qualifier.ScalpelWireframeEnabled;
import com.anlijiu.example.data.di.qualifier.SeenDebugDrawer;
import com.anlijiu.example.data.misc.InetSocketAddressPreferenceAdapter;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import okhttp3.OkHttpClient;

@Module(includes = {DataModule.class})
public final class DebugDataModule {
    private static final int DEFAULT_ANIMATION_SPEED = 1; // 1x (normal) speed.
    private static final boolean DEFAULT_PIXEL_GRID_ENABLED = false; // No pixel grid overlay.
    private static final boolean DEFAULT_PIXEL_RATIO_ENABLED = false; // No pixel ratio overlay.
    private static final boolean DEFAULT_SCALPEL_ENABLED = false; // No crazy 3D view tree.
    private static final boolean DEFAULT_SCALPEL_WIREFRAME_ENABLED = false; // Draw views by
    private static final boolean DEFAULT_SEEN_DEBUG_DRAWER = false; // Show debug drawer first time.
    private static final boolean DEFAULT_CAPTURE_INTENTS = true; // Capture external intents.
    private static final String API_POINT = "https://api.github.com";

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
    OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder,
                                     Preference<InetSocketAddress> networkProxyAddress) {
        return builder
//                 .addNetworkInterceptor(new StethoInterceptor())
                .sslSocketFactory(createBadSslSocketFactory())
//                .proxy(InetSocketAddressPreferenceAdapter.createProxy(networkProxyAddress.get()))
                .build();
    }

    @Provides
    @Singleton
    @ApiEndpoint
    Preference<String> provideEndpointPreference(RxSharedPreferences prefs) {
        return prefs.getString("debug_endpoint", API_POINT);
    }

    @Provides
    @Singleton
    @Named("base_endpoint")
    Preference<String> provideBaseEndpointPreference(RxSharedPreferences prefs) {
        return prefs.getString("debug_base_endpoint", ApiEndpoints.BASE.url);
    }

    @Provides
    @Singleton
    @IsMockMode
    boolean provideIsMockMode(@ApiEndpoint Preference<String> endpoint) {
        return ApiEndpoints.isMockMode(endpoint.get());
    }


    @Provides
    @Singleton
    @NetworkDelay
    Preference<Long> provideNetworkDelay(RxSharedPreferences prefs) {
        return prefs.getLong("debug_network_delay", 2000L);
    }

    @Provides
    @Singleton
    @NetworkFailurePercent
    Preference<Integer> provideNetworkFailurePercent(RxSharedPreferences prefs) {
        return prefs.getInteger("debug_network_failure_percent", 3);
    }

    @Provides
    @Singleton
    @NetworkVariancePercent
    Preference<Integer> provideNetworkVariancePercent(RxSharedPreferences prefs) {
        return prefs.getInteger("debug_network_variance_percent", 40);
    }

    @Provides
    @Singleton
    Preference<InetSocketAddress> provideNetworkProxyAddress(RxSharedPreferences preferences) {
        return preferences.getObject("debug_network_proxy", InetSocketAddress.createUnresolved("0.0.0.0", 0), InetSocketAddressPreferenceAdapter.INSTANCE);
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


    private static SSLSocketFactory createBadSslSocketFactory() {
        try {
            // Construct SSLSocketFactory that accepts any cert.
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager permissive = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            context.init(null, new TrustManager[]{permissive}, null);
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
