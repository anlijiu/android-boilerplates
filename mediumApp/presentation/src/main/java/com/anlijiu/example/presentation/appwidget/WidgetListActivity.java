package com.anlijiu.example.presentation.appwidget;

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

public class WidgetListActivity extends BaseMvpActivity<WidgetListView, WidgetListPresenter, WidgetListViewModel> implements WidgetListView {

    @Inject
    WidgetListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @NonNull
    @Override
    protected WidgetListPresenter presenter() {
        return presenter;
    }

    @Override
    protected Class<WidgetListViewModel> viewModelClass() {
        return WidgetListViewModel.class;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_widget_list;
    }

}
