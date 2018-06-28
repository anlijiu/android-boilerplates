package com.anlijiu.example.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AbstractAppWidget {
    public abstract String label();
    public abstract int previewImage();
    public abstract int icon();
    public abstract int widgetLayout();

    public abstract int spanX();
    public abstract int spanY();
    public abstract int minSpanX();
    public abstract int minSpanY();
    public abstract int resizeMode();
}
