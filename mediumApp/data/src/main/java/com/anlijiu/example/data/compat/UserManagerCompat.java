package com.anlijiu.example.data.compat;

import android.content.Context;
import android.os.Build;
import android.os.UserHandle;


import java.util.List;

public abstract class UserManagerCompat {
    protected UserManagerCompat() {
    }

    /**
     * Creates a cache for users.
     */
    public abstract void enableAndResetCache();

    public abstract List<UserHandle> getUserProfiles();
    public abstract long getSerialNumberForUser(UserHandle user);
    public abstract UserHandle getUserForSerialNumber(long serialNumber);
    public abstract CharSequence getBadgedLabelForUser(CharSequence label, UserHandle user);
    public abstract long getUserCreationTime(UserHandle user);
    public abstract boolean isQuietModeEnabled(UserHandle user);
    public abstract boolean isUserUnlocked(UserHandle user);

    public abstract boolean isDemoUser();
}
