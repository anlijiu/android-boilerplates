package com.anlijiu.example.ui.bindingadapter.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.databinding.BindingAdapter;
import android.support.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

public class ViewBindingAdapter {

    @BindingAdapter("animatedVisibility")
    public static void setVisibility(final View target, boolean isVisible ) {
        ViewGroup rootView = (ViewGroup) target.getRootView();
        TransitionManager.beginDelayedTransition(rootView);
        target.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
