package com.anlijiu.example.presentation.appwidget;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.anlijiu.example.R;
import com.anlijiu.example.ui.recyclerview.BaseMvvmRecyclerViewAdapter;


/**
 * Created by anlijiu on 18-3-29.
 */

public class WidgetListAdapter<T> extends BaseMvvmRecyclerViewAdapter<T, ViewDataBinding> {
    @Override
    public void onViewDetachedFromWindow(ViewHolder<T, ViewDataBinding> holder) {
        ViewGroup rootView = (ViewGroup) holder.itemView;
        ViewGroup container = rootView.findViewById(R.id.widget_container);
        container.removeAllViews();
        super.onViewDetachedFromWindow(holder);

    }
        @Override
    public void onViewAttachedToWindow(ViewHolder<T, ViewDataBinding> holder) {

        if(holder.getItem()!= null && holder.getItem() instanceof WidgetModel) {
            View view =  ((WidgetModel) holder.getItem()).widgetView();
            ViewGroup viewParent = (ViewGroup) view.getParent();
            if(viewParent != null) {
                viewParent.removeAllViews();
            }
            ViewGroup rootView = (ViewGroup) holder.itemView;
            ViewGroup container = rootView.findViewById(R.id.widget_container);
            container.addView(view);
        }
        super.onViewAttachedToWindow(holder);
    }
}
