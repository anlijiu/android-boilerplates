package com.anlijiu.example.data;

import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Process;
import android.os.UserHandle;

/**
 * This class is a thin wrapper around the framework AppWidgetProviderInfo class. This class affords
 * a common object for describing both framework provided AppWidgets as well as custom widgets
 * (who's implementation is owned by the launcher). This object represents a widget type / class,
 * as opposed to a widget instance
 */
public class LauncherAppWidgetProviderInfo extends AppWidgetProviderInfo {

    public boolean isCustomWidget = false;

    public int spanX;
    public int spanY;
    public int minSpanX;
    public int minSpanY;

    public static LauncherAppWidgetProviderInfo fromProviderInfo(Context context,
            AppWidgetProviderInfo info) {
        final LauncherAppWidgetProviderInfo launcherInfo;
        if (info instanceof LauncherAppWidgetProviderInfo) {
            launcherInfo = (LauncherAppWidgetProviderInfo) info;
        } else {

            // In lieu of a public super copy constructor, we first write the AppWidgetProviderInfo
            // into a parcel, and then construct a new LauncherAppWidgetProvider info from the
            // associated super parcel constructor. This allows us to copy non-public members without
            // using reflection.
            Parcel p = Parcel.obtain();
            info.writeToParcel(p, 0);
            p.setDataPosition(0);
            launcherInfo = new LauncherAppWidgetProviderInfo(p);
            p.recycle();
        }
        return launcherInfo;
    }

    private LauncherAppWidgetProviderInfo(Parcel in) {
        super(in);
    }

    public LauncherAppWidgetProviderInfo(Context context, AbstractAppWidget widget) {
        isCustomWidget = true;

        provider = new ComponentName(context, widget.getClass().getName());
        icon = widget.icon();
        label = widget.label();
        previewImage = widget.previewImage();
        initialLayout = widget.widgetLayout();
        resizeMode = widget.resizeMode();
    }

    public void initSpans(Context context) {

    }

    public String toString(PackageManager pm) {
        if (isCustomWidget) {
            return "WidgetProviderInfo(" + provider + ")";
        }
        return String.format("WidgetProviderInfo provider:%s package:%s short:%s ",
                provider.toString(), provider.getPackageName(), provider.getShortClassName());
    }

    public UserHandle getUser() {
        return isCustomWidget ? Process.myUserHandle() : getProfile();
    }
 }
