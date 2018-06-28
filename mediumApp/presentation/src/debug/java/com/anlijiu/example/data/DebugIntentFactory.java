package com.anlijiu.example.data;

import android.content.Intent;


import com.anlijiu.example.ui.ExternalIntentActivity;
import com.anlijiu.example.utils.IntentFactory;
import com.f2prateek.rx.preferences2.Preference;


/**
 * An {@link IntentFactory} implementation that wraps all {@code Intent}s with a debug action, which
 * launches an activity that allows you to inspect the content.
 */
public final class DebugIntentFactory implements IntentFactory {
    private final IntentFactory realIntentFactory;
    private final Preference<Boolean> captureIntents;

    public DebugIntentFactory(IntentFactory realIntentFactory, Preference<Boolean> captureIntents) {
        this.realIntentFactory = realIntentFactory;
        this.captureIntents = captureIntents;
    }

    @Override
    public Intent createUrlIntent(String url) {
        Intent baseIntent = realIntentFactory.createUrlIntent(url);
        if (!captureIntents.get()) {
            return baseIntent;
        } else {
            return ExternalIntentActivity.createIntent(baseIntent);
        }
    }
}
