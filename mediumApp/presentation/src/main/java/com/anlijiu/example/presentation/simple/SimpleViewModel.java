package com.anlijiu.example.presentation.simple;

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

public class SimpleViewModel extends BaseViewModel {
    public final MutableLiveData<String> textValue = new MutableLiveData<>();
    public final SparseArray<AdapterDelegate> delegates = new SparseArray<AdapterDelegate>();
    public final SimpleListAdapter adapter = new SimpleListAdapter();
    public final MutableLiveData<List<SimpleItem>> items = new MutableLiveData<>();

    private int count = 0;

    public SimpleViewModel() {
        Timber.e("ssss SimpleViewModel constructor in !");
        delegates.append(R.layout.item_simple, new BaseAdapterDelegate(SimpleItem.class));
        textValue.setValue("hahahahah");
    }

    public void updateItems(List<SimpleItem> items) {
        Timber.d("ssss SimpleViewModel updateItems size is %d", items.size());
        for(int i = 0; i < items.size(); i++) {
            Timber.d("item is %d, name is %s", items.get(i).id(), items.get(i).name());
        }

        this.items.setValue(items);

        textValue.setValue("hahahahah " + items.size());
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int increase() {
        count++;
        return count;
    }

    public void onClickLoadData(View view) {
        Timber.e("ssss SimpleViewModel onClickLoadData");
        count ++;
        textValue.setValue("hahahahah" + count);
    }
}
