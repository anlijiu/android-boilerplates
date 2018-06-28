package com.anlijiu.example.data.compat;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.support.annotation.Nullable;

import com.anlijiu.example.data.config.FeatureFlags;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppWidgetManagerCompatVO extends AppWidgetManagerCompatVL {

    @Inject
    AppWidgetManagerCompatVO(Context context) {
        super(context);
    }

    @Override
    public List<AppWidgetProviderInfo> getAllProviders(@Nullable PackageUserKey packageUser) {
        if (FeatureFlags.GO_DISABLE_WIDGETS) {
            return Collections.emptyList();
        }
        if (packageUser == null) {
            return super.getAllProviders(null);
        }
        return super.getAllProviders(packageUser);
//         return mAppWidgetManager.getInstalledProvidersForPackage(packageUser.mPackageName,
//                 packageUser.mUser);
    }
}
