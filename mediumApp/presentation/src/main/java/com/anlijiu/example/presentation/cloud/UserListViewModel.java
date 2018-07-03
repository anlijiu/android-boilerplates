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

        textValue.setValue("hahahahah " + items.size());
    }
}
