package com.anlijiu.example.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;

import com.anlijiu.example.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;

@Singleton
public final class ReleaseViewContainer implements ViewContainer {

    static class ViewHolder {
        @BindView(R.id.release_content) ViewGroup content;
    }

    @Inject
    public ReleaseViewContainer() {
    }

    @Override
    public ViewGroup forActivity(final Activity activity) {
        activity.setContentView(R.layout.release_activity_frame);

        final ViewHolder viewHolder = new ViewHolder();
        ButterKnife.bind(viewHolder, activity);

        return viewHolder.content;
    }
}
