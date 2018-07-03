package com.anlijiu.example.presentation.appwidget;

import android.arch.lifecycle.MutableLiveData;
import android.util.SparseArray;
import android.view.View;

import com.anlijiu.example.domain.objects.SimpleItem;
import com.anlijiu.example.ui.bindingadapter.recyclerview.DiffObservableList;
import com.anlijiu.example.ui.recyclerview.AdapterDelegate;
import com.anlijiu.example.ui.recyclerview.BaseAdapterDelegate;

import com.anlijiu.example.R;

import com.example.mvp.BaseViewModel;

import java.util.List;

import timber.log.Timber;

public class WidgetListViewModel extends BaseViewModel {
    public final SparseArray<AdapterDelegate> delegates = new SparseArray<AdapterDelegate>();
    public final WidgetListAdapter adapter = new WidgetListAdapter();
    public final MutableLiveData<List<WidgetModel>> items = new MutableLiveData<>();

    private int count = 0;

    public WidgetListViewModel() {
        Timber.e("ssss SimpleViewModel constructor in !");
        delegates.append(R.layout.item_widget, new BaseAdapterDelegate(WidgetModel.class));
    }

    public void updateItems(List<WidgetModel> items) {
        Timber.d("ssss SimpleViewModel updateItems size is %d", items.size());
        this.items.setValue(items);
    }
}
