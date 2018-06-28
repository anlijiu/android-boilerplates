package com.anlijiu.example.data.compat;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.Nullable;

import java.util.List;

public abstract class LauncherAppsCompat {

    public interface OnAppsChangedCallbackCompat {
        void onPackageRemoved(String packageName, UserHandle user);

        void onPackageAdded(String packageName, UserHandle user);

        void onPackageChanged(String packageName, UserHandle user);

        void onPackagesAvailable(String[] packageNames, UserHandle user, boolean replacing);

        void onPackagesUnavailable(String[] packageNames, UserHandle user, boolean replacing);

        void onPackagesSuspended(String[] packageNames, UserHandle user);

        void onPackagesUnsuspended(String[] packageNames, UserHandle user);

        void onShortcutsChanged(String packageName, List<ShortcutInfoCompat> shortcuts,
                                UserHandle user);
    }

    public abstract List<LauncherActivityInfo> getActivityList(String packageName,
                                                               UserHandle user);

    public abstract LauncherActivityInfo resolveActivity(Intent intent,
                                                         UserHandle user);

    public abstract void startActivityForProfile(ComponentName component, UserHandle user,
                                                 Rect sourceBounds, Bundle opts);

    public abstract ApplicationInfo getApplicationInfo(
            String packageName, int flags, UserHandle user);

    public abstract void showAppDetailsForProfile(ComponentName component, UserHandle user,
                                                  Rect sourceBounds, Bundle opts);

    public abstract void addOnAppsChangedCallback(OnAppsChangedCallbackCompat listener);

    public abstract void removeOnAppsChangedCallback(OnAppsChangedCallbackCompat listener);

    public abstract boolean isPackageEnabledForProfile(String packageName, UserHandle user);

    public abstract boolean isActivityEnabledForProfile(ComponentName component,
                                                        UserHandle user);

    public abstract List<ShortcutConfigActivityInfo> getCustomShortcutActivityList(
            @Nullable PackageUserKey packageUser);

    public void showAppDetailsForProfile(ComponentName component, UserHandle user) {
        showAppDetailsForProfile(component, user, null, null);
    }
}
