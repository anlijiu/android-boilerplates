package com.anlijiu.example.data.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.UserHandle;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@TargetApi(Build.VERSION_CODES.N)
public class UserManagerCompatVN extends UserManagerCompatVM {

    @Inject
    UserManagerCompatVN(Context context) {
        super(context);
    }

    @Override
    public boolean isQuietModeEnabled(UserHandle user) {
        return mUserManager.isQuietModeEnabled(user);
    }

    @Override
    public boolean isUserUnlocked(UserHandle user) {
        return mUserManager.isUserUnlocked(user);
    }
}

