package com.anlijiu.example.presentation.cloud;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mvp.BaseMvpActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

import com.anlijiu.example.R;

public class UserListActivity extends BaseMvpActivity<UserListView, UserListPresenter, UserListViewModel> implements UserListView {

    @Inject
    UserListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @NonNull
    @Override
    protected UserListPresenter presenter() {
        return presenter;
    }

    @Override
    protected Class<UserListViewModel> viewModelClass() {
        return UserListViewModel.class;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_user_list;
    }

    @Override
    public Observable<Object> increaseCount() {
        return null;
        // return RxView.clicks(btnAdd).throttleFirst(1, TimeUnit.SECONDS);
    }
}
