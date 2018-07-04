package com.anlijiu.example.presentation.cloud;

import android.arch.lifecycle.MutableLiveData;
import android.util.SparseArray;
import android.view.View;

import com.anlijiu.example.domain.objects.User;
import com.anlijiu.example.ui.bindingadapter.recyclerview.DiffObservableList;
import com.anlijiu.example.ui.recyclerview.AdapterDelegate;
import com.anlijiu.example.ui.recyclerview.BaseAdapterDelegate;

import com.anlijiu.example.R;

import com.example.mvp.BaseViewModel;

import java.util.List;

import timber.log.Timber;

public class UserListViewModel extends BaseViewModel {
    public final MutableLiveData<String> textValue = new MutableLiveData<>();
    public final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public final MutableLiveData<Boolean> refreshing = new MutableLiveData<>();
    public final SparseArray<AdapterDelegate> delegates = new SparseArray<AdapterDelegate>();
    public final UserListAdapter adapter = new UserListAdapter();
    public final MutableLiveData<List<User>> items = new MutableLiveData<>();

    private int count = 0;

    public UserListViewModel() {
        Timber.e("ssss UserListViewModel constructor in !");
        delegates.append(R.layout.item_user, new BaseAdapterDelegate(User.class));
        textValue.setValue("hahahahah");
    }

    public void updateItems(List<User> items) {
        Timber.d("ssss UserListViewModel updateItems size is %d", items.size());
        for(int i = 0; i < items.size(); i++) {
            Timber.d("item id is %s", items.get(i).id());
        }

        this.items.setValue(items);
        textValue.setValue("item size is " + items.size());
        loading.setValue(false);
        refreshing.setValue(false);
    }

    public void addItems(List<User> items) {
        Timber.d("ssss UserListViewModel addItems size is %d", items.size());
        for(int i = 0; i < items.size(); i++) {
            Timber.d("item id is %s", items.get(i).id());
        }
        List<User> orig = this.items.getValue();
        orig.addAll(items);

        this.items.setValue(orig);
        textValue.setValue("item size is " + this.items.getValue().size());
        loading.setValue(false);
    }

    public int size() {
        return items.getValue() == null ? 0 : items.getValue().size();
    }

    public boolean isEmpty() {
        return items.getValue() == null ? true : items.getValue().isEmpty();
    }

    public int lastUserId() {
        return isEmpty() ? 1 : items.getValue().get(items.getValue().size()-1).id();
    }
}
