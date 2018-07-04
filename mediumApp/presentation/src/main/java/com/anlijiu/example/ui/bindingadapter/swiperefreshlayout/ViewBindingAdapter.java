package com.anlijiu.example.ui.bindingadapter.swiperefreshlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.databinding.BindingAdapter;
import android.support.transition.TransitionManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

public class ViewBindingAdapter {

    @BindingAdapter("refreshing")
    public static void setVisibility(final SwipeRefreshLayout target, boolean isRefreshing ) {
        target.setRefreshing(isRefreshing);
    }
}
