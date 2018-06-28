package com.anlijiu.example.data.compat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;

import com.anlijiu.example.utils.LongArrayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserManagerCompatVL extends UserManagerCompat {
    private static final String USER_CREATION_TIME_KEY = "user_creation_time_";

    protected final UserManager mUserManager;
    private final PackageManager mPm;
    private final Context mContext;

    protected LongArrayMap<UserHandle> mUsers;
    // Create a separate reverse map as LongArrayMap.indexOfValue checks if objects are same
    // and not {@link Object#equals}
    protected ArrayMap<UserHandle, Long> mUserToSerialMap;

    @Inject
    UserManagerCompatVL(Context context) {
        mUserManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        mPm = context.getPackageManager();
        mContext = context;
    }

    @Override
    public long getSerialNumberForUser(UserHandle user) {
        synchronized (this) {
            if (mUserToSerialMap != null) {
                Long serial = mUserToSerialMap.get(user);
                return serial == null ? 0 : serial;
            }
        }
        return mUserManager.getSerialNumberForUser(user);
    }

    @Override
    public UserHandle getUserForSerialNumber(long serialNumber) {
        synchronized (this) {
            if (mUsers != null) {
                return mUsers.get(serialNumber);
            }
        }
        return mUserManager.getUserForSerialNumber(serialNumber);
    }

    @Override
    public boolean isQuietModeEnabled(UserHandle user) {
        return false;
    }

    @Override
    public boolean isUserUnlocked(UserHandle user) {
        return true;
    }

    @Override
    public boolean isDemoUser() {
        return false;
    }

    @Override
    public void enableAndResetCache() {
        synchronized (this) {
            mUsers = new LongArrayMap<>();
            mUserToSerialMap = new ArrayMap<>();
            List<UserHandle> users = mUserManager.getUserProfiles();
            if (users != null) {
                for (UserHandle user : users) {
                    long serial = mUserManager.getSerialNumberForUser(user);
                    mUsers.put(serial, user);
                    mUserToSerialMap.put(user, serial);
                }
            }
        }
    }

    @Override
    public List<UserHandle> getUserProfiles() {
        synchronized (this) {
            if (mUsers != null) {
                return new ArrayList<>(mUserToSerialMap.keySet());
            }
        }

        List<UserHandle> users = mUserManager.getUserProfiles();
        return users == null ? Collections.<UserHandle>emptyList() : users;
    }

    @Override
    public CharSequence getBadgedLabelForUser(CharSequence label, UserHandle user) {
        if (user == null) {
            return label;
        }
        return mPm.getUserBadgedLabel(label, user);
    }

    @Override
    public long getUserCreationTime(UserHandle user) {
        //TODO
        return 0;
//        SharedPreferences prefs = ManagedProfileHeuristic.prefs(mContext);
//        String key = USER_CREATION_TIME_KEY + getSerialNumberForUser(user);
//        if (!prefs.contains(key)) {
//            prefs.edit().putLong(key, System.currentTimeMillis()).apply();
//        }
//        return prefs.getLong(key, 0);
    }
}

