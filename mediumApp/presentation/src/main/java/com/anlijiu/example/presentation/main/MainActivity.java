package com.anlijiu.example.presentation.main;

import android.content.Intent;
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

import com.anlijiu.example.di.DaggerAppComponent;
import com.anlijiu.example.presentation.appwidget.WidgetListActivity;
import com.anlijiu.example.presentation.cloud.UserListActivity;
import com.anlijiu.example.presentation.simple.SimpleActivity;
import com.example.mvp.BaseMvpActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;

import com.anlijiu.example.R;

public class MainActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @OnClick({R.id.btn_simple, R.id.btn_appwidget, R.id.btn_userlist})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_simple:
                startActivity(new Intent(this, SimpleActivity.class));
                break;
            case R.id.btn_appwidget:
                startActivity(new Intent(this, WidgetListActivity.class));
            case R.id.btn_userlist:
                startActivity(new Intent(this, UserListActivity.class));
            default:
                break;
        }
    }

}
