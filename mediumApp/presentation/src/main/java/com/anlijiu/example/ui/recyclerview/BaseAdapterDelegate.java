package com.anlijiu.example.ui.recyclerview;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by anlijiu on 16-8-12.
 */
public class BaseAdapterDelegate<T> implements AdapterDelegate<List<T>> {
    Class<?> cls;
    public BaseAdapterDelegate(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public boolean isForViewType(@NonNull List<T> items, int position) {
        return cls.isInstance(items.get(position));
    }
}
