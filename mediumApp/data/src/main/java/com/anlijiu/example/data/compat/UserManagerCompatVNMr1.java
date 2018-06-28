package com.anlijiu.example.data.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@TargetApi(Build.VERSION_CODES.N_MR1)
public class UserManagerCompatVNMr1 extends UserManagerCompatVN {

    @Inject
    UserManagerCompatVNMr1(Context context) {
        super(context);
    }

    @Override
    public boolean isDemoUser() {
        return mUserManager.isDemoUser();
    }
}
