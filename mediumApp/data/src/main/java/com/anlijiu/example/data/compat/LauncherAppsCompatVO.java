package com.anlijiu.example.data.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.LauncherApps.PinItemRequest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.os.Process;
import android.os.UserHandle;
import android.support.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@TargetApi(26)
public class LauncherAppsCompatVO extends LauncherAppsCompatVL {

    UserManagerCompat userManagerCompat;

    @Inject
    LauncherAppsCompatVO(Context context, UserManagerCompat userManagerCompat) {
        super(context);
        this.userManagerCompat = userManagerCompat;
    }

    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags, UserHandle user) {
        try {
            ApplicationInfo info = mLauncherApps.getApplicationInfo(packageName, flags, user);
            return (info.flags & ApplicationInfo.FLAG_INSTALLED) == 0 || !info.enabled
                    ? null : info;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public List<ShortcutConfigActivityInfo> getCustomShortcutActivityList(
            @Nullable PackageUserKey packageUser) {
        List<ShortcutConfigActivityInfo> result = new ArrayList<>();
        UserHandle myUser = Process.myUserHandle();

        final List<UserHandle> users;
        final String packageName;
        if (packageUser == null) {
            users = userManagerCompat.getUserProfiles();
            packageName = null;
        } else {
            users = new ArrayList<>(1);
            users.add(packageUser.mUser);
            packageName = packageUser.mPackageName;
        }
        for (UserHandle user : users) {
            boolean ignoreTargetSdk = myUser.equals(user);
            List<LauncherActivityInfo> activities =
                    mLauncherApps.getShortcutConfigActivityList(packageName, user);
            for (LauncherActivityInfo activityInfo : activities) {
                if (ignoreTargetSdk || activityInfo.getApplicationInfo().targetSdkVersion >=
                        Build.VERSION_CODES.O) {
                    result.add(new ShortcutConfigActivityInfo.ShortcutConfigActivityInfoVO(activityInfo));
                }
            }
        }

        return result;
    }

    public static PinItemRequest getPinItemRequest(Intent intent) {
        Parcelable extra = intent.getParcelableExtra(LauncherApps.EXTRA_PIN_ITEM_REQUEST);
        return extra instanceof PinItemRequest ? (PinItemRequest) extra : null;
    }
}
