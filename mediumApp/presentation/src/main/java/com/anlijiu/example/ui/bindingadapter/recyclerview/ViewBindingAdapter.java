package com.anlijiu.example.ui.bindingadapter.recyclerview;

import android.databinding.BindingAdapter;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.widget.RadioButton;

import com.anlijiu.example.ui.recyclerview.AdapterDelegate;
import com.anlijiu.example.ui.recyclerview.BaseMvvmRecyclerViewAdapter;
import com.anlijiu.example.ui.recyclerview.LayoutManagers;
import com.anlijiu.example.ui.recyclerview.OnItemClickListener;
import com.anlijiu.example.ui.recyclerview.RecyclerViewAdapterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;


public class ViewBindingAdapter {

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemLayout", "delegates", "items", "adapter", "onItemClickLisener", "viewModel"}, requireAll = false)
    public static  <T, VM>void setDelegates(RecyclerView recyclerView, @LayoutRes int layoutId, SparseArray<AdapterDelegate> delegates, List<T> items,
                                                               BaseMvvmRecyclerViewAdapter adapter, OnItemClickListener listener, VM viewModel) {
        if (layoutId == 0 && delegates == null) {
            throw new IllegalArgumentException("DataBindingAdapter: BaseMvvmAdapter delegates must not be null");
        }
        RecyclerViewAdapterFactory factory = RecyclerViewAdapterFactory.DEFAULT;
//        if (factory == null) {
//            factory = RecyclerViewAdapterFactory.DEFAULT;
//        }
        BaseMvvmRecyclerViewAdapter oldAdapter = (BaseMvvmRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = factory.create(recyclerView, items);
            } else {
                adapter = oldAdapter;
            }
        }
        adapter.setItems(items);
        if(layoutId != 0) {
            adapter.setItemLayout(layoutId);
        } else {
            adapter.setDelegates(delegates);
        }

        adapter.setOnItemClickListener(listener);
        adapter.setViewModel(viewModel);
        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }
}
