package com.anlijiu.example.presentation.simple;

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

import com.anlijiu.example.databinding.ActivitySimpleBinding;
import com.example.mvp.BaseMvpActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

import com.anlijiu.example.R;

public class SimpleActivity extends BaseMvpActivity<SimpleView, SimplePresenter, SimpleViewModel> implements SimpleView {

    @Inject
    SimplePresenter presenter;

    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @NonNull
    @Override
    protected SimplePresenter presenter() {
        return presenter;
    }

    @Override
    protected Class<SimpleViewModel> viewModelClass() {
        return SimpleViewModel.class;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_simple;
    }

    @Override
    public Observable<Object> increaseCount() {
        return RxView.clicks(btnAdd).throttleFirst(1, TimeUnit.SECONDS);
    }
}
