package com.anlijiu.example.data.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.UserHandle;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@TargetApi(Build.VERSION_CODES.M)
public class UserManagerCompatVM extends UserManagerCompatVL {

    @Inject
    UserManagerCompatVM(Context context) {
        super(context);
    }

    @Override
    public long getUserCreationTime(UserHandle user) {
        return mUserManager.getUserCreationTime(user);
    }
}
