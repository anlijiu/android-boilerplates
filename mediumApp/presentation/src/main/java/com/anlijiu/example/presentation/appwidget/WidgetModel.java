package com.anlijiu.example.presentation.appwidget;

import android.support.annotation.Nullable;
import android.view.View;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class WidgetModel {
    public abstract long id();
    @Nullable
    public abstract View widgetView();

    public static WidgetModel create(long id, View widgetView) {
        return builder()
                .id(id)
                .widgetView(widgetView)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_WidgetModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder widgetView(View widgetView);
        public abstract Builder id(long id);

        public abstract WidgetModel build();
    }
}