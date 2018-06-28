package com.anlijiu.example.ui.recyclerview;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;


import java.net.IDN;
import java.util.List;

public interface RecyclerViewAdapterFactory {
    <T>BaseMvvmRecyclerViewAdapter create(RecyclerView recyclerView, List<T> arg);

    RecyclerViewAdapterFactory DEFAULT = new RecyclerViewAdapterFactory() {
        @Override
        public <T>BaseMvvmRecyclerViewAdapter create(RecyclerView recyclerView, List<T> arg) {
            return new BaseMvvmRecyclerViewAdapter(arg);
        }
    };
}
