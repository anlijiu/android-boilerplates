package com.anlijiu.example.data.compat;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.annotation.Nullable;


import com.anlijiu.example.data.LauncherAppWidgetProviderInfo;
import com.anlijiu.example.data.config.FeatureFlags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
class AppWidgetManagerCompatVL extends AppWidgetManagerCompat {

    private final UserManager mUserManager;

    @Inject
    AppWidgetManagerCompatVL(Context context) {
        super(context);
        mUserManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
    }

    @Override
    public List<AppWidgetProviderInfo> getAllProviders(@Nullable PackageUserKey packageUser) {
        if (FeatureFlags.GO_DISABLE_WIDGETS) {
            return Collections.emptyList();
        }
        if (packageUser == null) {
            ArrayList<AppWidgetProviderInfo> providers = new ArrayList<AppWidgetProviderInfo>();
            for (UserHandle user : mUserManager.getUserProfiles()) {
                providers.addAll(mAppWidgetManager.getInstalledProvidersForProfile(user));
            }
            for(AppWidgetProviderInfo info : providers) {
                Timber.d("AppWidgetProviderInfo.provider is %s", info.provider);
            }
            return providers;
        }
        // Only get providers for the given package/user.
        List<AppWidgetProviderInfo> providers = new ArrayList<>(mAppWidgetManager
                .getInstalledProvidersForProfile(packageUser.mUser));
        Timber.d("ssss getAllProviders packageName is %s, providers size is %d",packageUser.mPackageName, providers.size());
        Iterator<AppWidgetProviderInfo> iterator = providers.iterator();
        while (iterator.hasNext()) {
            String packageName = iterator.next().provider.getPackageName();
            Timber.d("ssss while()iterator.hasNext() in , iterator.next().provider.getPackageName() is %s", packageName);
            if (!packageName.equals(packageUser.mPackageName)) {
                iterator.remove();
            }
        }

        Timber.d("ssss getAllProviders at last  providers size is %d", providers.size());
        return providers;
    }

    @Override
    public boolean bindAppWidgetIdIfAllowed(int appWidgetId, AppWidgetProviderInfo info,
            Bundle options) {
        if (FeatureFlags.GO_DISABLE_WIDGETS) {
            return false;
        }
        return mAppWidgetManager.bindAppWidgetIdIfAllowed(
                appWidgetId, info.getProfile(), info.provider, options);
    }

    @Override
    public LauncherAppWidgetProviderInfo findProvider(ComponentName provider, UserHandle user) {
        if (FeatureFlags.GO_DISABLE_WIDGETS) {
            return null;
        }
        for (AppWidgetProviderInfo info :
                getAllProviders(new PackageUserKey(provider.getPackageName(), user))) {
            if (info.provider.equals(provider)) {
                return LauncherAppWidgetProviderInfo.fromProviderInfo(mContext, info);
            }
        }
        return null;
    }

    @Override
    public HashMap<ComponentKey, AppWidgetProviderInfo> getAllProvidersMap() {
        HashMap<ComponentKey, AppWidgetProviderInfo> result = new HashMap<>();
        if (FeatureFlags.GO_DISABLE_WIDGETS) {
            return result;
        }
        for (UserHandle user : mUserManager.getUserProfiles()) {
            for (AppWidgetProviderInfo info :
                    mAppWidgetManager.getInstalledProvidersForProfile(user)) {
                result.put(new ComponentKey(info.provider, user), info);
            }
        }
        return result;
    }
}
