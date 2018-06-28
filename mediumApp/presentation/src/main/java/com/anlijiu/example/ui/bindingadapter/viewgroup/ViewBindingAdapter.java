package com.anlijiu.example.ui.bindingadapter.viewgroup;

import android.databinding.BindingAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public final class ViewBindingAdapter {

    @BindingAdapter({"child"})
    public static void child(ViewGroup parent, final View child) {
        if(child.getParent() == null) {
            parent.addView(child);
        }
    }
}

