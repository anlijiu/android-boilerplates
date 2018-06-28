package com.anlijiu.example.ui.debug;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.anlijiu.example.di.AndroidViewInjection;
import com.anlijiu.example.data.di.qualifier.AnimationSpeed;
import com.anlijiu.example.data.di.qualifier.CaptureIntents;
import com.anlijiu.example.data.di.qualifier.IsMockMode;
import com.anlijiu.example.data.di.qualifier.NetworkDelay;
import com.anlijiu.example.data.di.qualifier.NetworkFailurePercent;
import com.anlijiu.example.data.di.qualifier.NetworkVariancePercent;
import com.anlijiu.example.data.di.qualifier.PixelGridEnabled;
import com.anlijiu.example.data.di.qualifier.PixelRatioEnabled;
import com.anlijiu.example.data.di.qualifier.ScalpelEnabled;
import com.anlijiu.example.data.di.qualifier.ScalpelWireframeEnabled;
import com.anlijiu.example.utils.StringsUtils;
import com.f2prateek.rx.preferences2.Preference;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.squareup.leakcanary.internal.DisplayLeakActivity;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAccessor;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.anlijiu.example.BuildConfig;
import com.anlijiu.example.R;

import com.anlijiu.example.data.LumberYard;


import com.anlijiu.example.ui.logs.LogsDialog;
import com.anlijiu.example.ui.misc.EnumAdapter;

import timber.log.Timber;

import static butterknife.ButterKnife.findById;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class DebugView extends FrameLayout {
    private static final DateTimeFormatter DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.US).withZone(ZoneId.systemDefault());

    @BindView(R.id.debug_contextual_title)
    View contextualTitleView;
    @BindView(R.id.debug_contextual_list)
    LinearLayout contextualListView;


    @BindView(R.id.debug_ui_animation_speed)
    Spinner uiAnimationSpeedView;
    @BindView(R.id.debug_ui_pixel_grid)
    Switch uiPixelGridView;
    @BindView(R.id.debug_ui_pixel_ratio)
    Switch uiPixelRatioView;
    @BindView(R.id.debug_ui_scalpel)
    Switch uiScalpelView;
    @BindView(R.id.debug_ui_scalpel_wireframe)
    Switch uiScalpelWireframeView;
    @BindView(R.id.debug_build_name)
    TextView buildNameView;

    @BindView(R.id.debug_build_code)
    TextView buildCodeView;
    @BindView(R.id.debug_build_sha)
    TextView buildShaView;
    @BindView(R.id.debug_build_date)
    TextView buildDateView;
    @BindView(R.id.debug_device_make)
    TextView deviceMakeView;

    @BindView(R.id.debug_device_model)
    TextView deviceModelView;
    @BindView(R.id.debug_device_resolution)
    TextView deviceResolutionView;
    @BindView(R.id.debug_device_density)
    TextView deviceDensityView;
    @BindView(R.id.debug_device_release)
    TextView deviceReleaseView;
    @BindView(R.id.debug_device_api)
    TextView deviceApiView;

    @Inject
    LumberYard lumberYard;

    @Inject
    @CaptureIntents
    Preference<Boolean> captureIntents;
    @Inject
    @AnimationSpeed
    Preference<Integer> animationSpeed;
    @Inject
    @PixelGridEnabled
    Preference<Boolean> pixelGridEnabled;
    @Inject
    @PixelRatioEnabled
    Preference<Boolean> pixelRatioEnabled;
    @Inject
    @ScalpelEnabled
    Preference<Boolean> scalpelEnabled;
    @Inject
    @ScalpelWireframeEnabled
    Preference<Boolean> scalpelWireframeEnabled;

    private final ContextualDebugActions contextualDebugActions;

    public DebugView(Context context) {
        this(context, null);
        Timber.d("DebugView(Context context)");
    }

    public DebugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Timber.d("DebugView(Context context, AttributeSet attrs)");
        // Inflate all of the controls and inject them.
        LayoutInflater.from(context).inflate(R.layout.debug_view_content, this);
        ButterKnife.bind(this);
        AndroidViewInjection.inject(this);

        Timber.d("DebugView onFinishInflate  scalpelEnabled is %s", scalpelEnabled);
        setupUserInterfaceSection();
        setupBuildSection();
        setupDeviceSection();
        Set<ContextualDebugActions.DebugAction<?>> debugActions = Collections.emptySet();
        contextualDebugActions = new ContextualDebugActions(this, debugActions);
    }

    public ContextualDebugActions getContextualDebugActions() {
        return contextualDebugActions;
    }

    public void onDrawerOpened() {
        Timber.d("DebugView onDrawerOpened");
    }


    private void setupUserInterfaceSection() {
        final AnimationSpeedAdapter speedAdapter = new AnimationSpeedAdapter(getContext());
        uiAnimationSpeedView.setAdapter(speedAdapter);
        final int animationSpeedValue = animationSpeed.get();
        uiAnimationSpeedView.setSelection(AnimationSpeedAdapter.getPositionForValue(animationSpeedValue));

        RxAdapterView.itemSelections(uiAnimationSpeedView)
                .map(speedAdapter::getItem)
                .filter(item -> item != animationSpeed.get())
                .subscribe(selected -> {
                    Timber.d("Setting animation speed to %sx", selected);
                    animationSpeed.set(selected);
                    applyAnimationSpeed(selected);
                });

        // Ensure the animation speed value is always applied across app restarts.
        post(() -> applyAnimationSpeed(animationSpeedValue));

        boolean gridEnabled = pixelGridEnabled.get();
        uiPixelGridView.setChecked(gridEnabled);
        uiPixelRatioView.setEnabled(gridEnabled);
        uiPixelGridView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Timber.d("Setting pixel grid overlay enabled to %b", isChecked);
            pixelGridEnabled.set(isChecked);
            uiPixelRatioView.setEnabled(isChecked);
        });

        uiPixelRatioView.setChecked(pixelRatioEnabled.get());
        uiPixelRatioView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Timber.d("Setting pixel scale overlay enabled to %b", isChecked);
            pixelRatioEnabled.set(isChecked);
        });

        uiScalpelView.setChecked(scalpelEnabled.get());
        uiScalpelWireframeView.setEnabled(scalpelEnabled.get());
        uiScalpelView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Timber.d("Setting scalpel interaction enabled to %b", isChecked);
            scalpelEnabled.set(isChecked);
            uiScalpelWireframeView.setEnabled(isChecked);
        });

        uiScalpelWireframeView.setChecked(scalpelWireframeEnabled.get());
        uiScalpelWireframeView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Timber.d("Setting scalpel wireframe enabled to %b", isChecked);
            scalpelWireframeEnabled.set(isChecked);
        });
    }

    @OnClick(R.id.debug_logs_show)
    void showLogs() {
        new LogsDialog(new ContextThemeWrapper(getContext(), R.style.Theme_M01_Debug), lumberYard).show();
    }

    @OnClick(R.id.debug_leaks_show)
    void showLeaks() {
        Intent intent = new Intent(getContext(), DisplayLeakActivity.class);
        getContext().startActivity(intent);
    }

    @OnClick(R.id.debug_set_wallpaper)
    void setWallpaper() {
        Intent chooseIntent = new Intent(Intent.ACTION_SET_WALLPAPER);
// 启动系统选择应用
        Intent intent = new Intent(Intent.ACTION_CHOOSER);
        intent.putExtra(Intent.EXTRA_INTENT, chooseIntent);
        intent.putExtra(Intent.EXTRA_TITLE, "选择壁纸");
        getContext().startActivity(intent);
    }

    private void setupBuildSection() {
        buildNameView.setText(BuildConfig.VERSION_NAME);
        buildCodeView.setText(String.valueOf(BuildConfig.VERSION_CODE));
        buildShaView.setText(BuildConfig.GIT_SHA);

        TemporalAccessor buildTime = Instant.ofEpochSecond(BuildConfig.GIT_TIMESTAMP);
        buildDateView.setText(DATE_DISPLAY_FORMAT.format(buildTime));
    }

    private void setupDeviceSection() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        String densityBucket = getDensityString(displayMetrics);
        deviceMakeView.setText(StringsUtils.truncateAt(Build.MANUFACTURER, 20));
        deviceModelView.setText(StringsUtils.truncateAt(Build.MODEL, 20));
        deviceResolutionView.setText(displayMetrics.heightPixels + "x" + displayMetrics.widthPixels);
        deviceDensityView.setText(displayMetrics.densityDpi + "dpi (" + densityBucket + ")");
        deviceReleaseView.setText(Build.VERSION.RELEASE);
        deviceApiView.setText(String.valueOf(Build.VERSION.SDK_INT));
    }

    private void applyAnimationSpeed(int multiplier) {
        try {
            Method method = ValueAnimator.class.getDeclaredMethod("setDurationScale", float.class);
            method.invoke(null, (float) multiplier);
        } catch (Exception e) {
            throw new RuntimeException("Unable to apply animation speed.", e);
        }
    }

    private static String getDensityString(DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return String.valueOf(displayMetrics.densityDpi);
        }
    }

    private static String getSizeString(long bytes) {
        String[] units = new String[]{"B", "KB", "MB", "GB"};
        int unit = 0;
        while (bytes >= 1024) {
            bytes /= 1024;
            unit += 1;
        }
        return bytes + units[unit];
    }
}
