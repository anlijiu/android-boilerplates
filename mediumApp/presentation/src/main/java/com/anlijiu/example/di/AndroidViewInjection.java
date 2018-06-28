package com.anlijiu.example.di;

/**
 * Created by anlijiu on 18-3-22.
 */

import android.content.Context;
import android.view.View;

import dagger.android.AndroidInjector;


public class AndroidViewInjection {
    @SuppressWarnings("unchecked")
    public static void inject(View view) {
        try {
            HasViewInjector hasViewInjector = findHasViewInjector(view);
            AndroidInjector injector = hasViewInjector.viewInjector();
            injector.inject(view);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static HasViewInjector findHasViewInjector(View view) throws IllegalAccessException {
        Context context = view.getContext();
        if(context instanceof HasViewInjector) {
            return (HasViewInjector)context;
        } else {
            if(context.getApplicationContext() instanceof HasViewInjector) {
                return (HasViewInjector) context.getApplicationContext();
            }
        }

        throw new IllegalAccessException("No injector was found for $view");
    }
}