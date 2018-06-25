package com.example.anlijiu.library;

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

import com.example.anlijiu.library.databinding.ActivityMainBinding;
import com.example.mvp.BaseMvpActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class MainActivity extends BaseMvpActivity<MainView, MainPresenter, MainViewModel> implements MainView {

    @Inject
    MainPresenter presenter;

    @BindView(R.id.btn_add)
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @NonNull
    @Override
    protected MainPresenter presenter() {
        return presenter;
    }

    @Override
    protected Class<MainViewModel> viewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Observable<Object> increaseCount() {
        return RxView.clicks(btnAdd).throttleFirst(1, TimeUnit.SECONDS);
    }
}
