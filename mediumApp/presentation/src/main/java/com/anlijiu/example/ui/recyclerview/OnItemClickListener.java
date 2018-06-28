package com.anlijiu.example.ui.recyclerview;

import android.view.View;

/**
 * Created by anlijiu on 16-8-4.
 */
public interface OnItemClickListener<T> {
    void onClick(View v, T item);
}
