package com.anlijiu.example.data.compat;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.Nullable;


import com.anlijiu.example.data.LauncherAppWidgetProviderInfo;

import java.util.HashMap;
import java.util.List;

public abstract class AppWidgetManagerCompat {
    final AppWidgetManager mAppWidgetManager;
    final Context mContext;

    AppWidgetManagerCompat(Context context) {
        mContext = context;
        mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    public AppWidgetProviderInfo getAppWidgetInfo(int appWidgetId) {
        return mAppWidgetManager.getAppWidgetInfo(appWidgetId);
    }

    public LauncherAppWidgetProviderInfo getLauncherAppWidgetInfo(int appWidgetId) {
        AppWidgetProviderInfo info = getAppWidgetInfo(appWidgetId);
        return info == null ? null : LauncherAppWidgetProviderInfo.fromProviderInfo(mContext, info);
    }

    public abstract List<AppWidgetProviderInfo> getAllProviders(
            @Nullable PackageUserKey packageUser);

    public abstract boolean bindAppWidgetIdIfAllowed(
            int appWidgetId, AppWidgetProviderInfo info, Bundle options);

    public abstract LauncherAppWidgetProviderInfo findProvider(
            ComponentName provider, UserHandle user);

    public abstract HashMap<ComponentKey, AppWidgetProviderInfo> getAllProvidersMap();
}
