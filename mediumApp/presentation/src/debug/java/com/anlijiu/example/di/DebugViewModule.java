package com.anlijiu.example.di;

import android.view.View;

import com.anlijiu.example.di.scope.ViewScope;
import com.anlijiu.example.ui.debug.DebugView;

import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by anlijiu on 18-3-22.
 */

@Module(subcomponents = {DebugViewModule.DebugViewInjectonViewComponent.class})
public abstract class DebugViewModule {
    @Binds
    @IntoMap
    @ViewKey(DebugView.class)
    abstract AndroidInjector.Factory<? extends View> bindMyListViewFactory(DebugViewInjectonViewComponent.Builder builder);

    @Subcomponent
    @ViewScope
    interface DebugViewInjectonViewComponent extends AndroidInjector<DebugView> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<DebugView> {

        }
    }

}
