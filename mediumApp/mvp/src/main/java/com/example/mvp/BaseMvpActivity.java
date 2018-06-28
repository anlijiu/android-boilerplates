package com.example.mvp;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;


public abstract class BaseMvpActivity<V extends IMvpView, P extends MvpPresenter<V, VM>, VM extends ViewModel>
        extends DaggerAppCompatActivity implements IMvpView {
    public static final int VIEW_ID_SELF = 0;
    private static final String BF_UNIQUE_KEY = BaseMvpActivity.class.getName() + ".unique.key";
    private String uniqueKey;
    protected ViewDataBinding binding;
    Unbinder unbinder;

    VM vm;
    P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProviders.of(this).get(viewModelClass());
        if (savedInstanceState != null && savedInstanceState.containsKey(BF_UNIQUE_KEY)) {
            uniqueKey = savedInstanceState.getString(BF_UNIQUE_KEY);
        } else {
            uniqueKey = UUID.randomUUID().toString();
        }

        presenter = presenter();

        final LayoutInflater layoutInflater = getLayoutInflater();
        View view = createView(layoutInflater, layoutId(), container());
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        unbinder.unbind();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(view(), viewModel());
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public String uniqueKey() {
        Timber.d("BaseMvpActivity uniqueKey() uniqueKey is %s", uniqueKey);
        return uniqueKey;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BF_UNIQUE_KEY, uniqueKey);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        uniqueKey = savedInstanceState.getString(BF_UNIQUE_KEY);
    }

    public final View createView(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup parent) {
        Timber.e("ssss BaseMvpActivity createView() in");
        this.binding = binding(inflater, layoutId, parent);
        return this.binding.getRoot();
    }

    protected ViewDataBinding binding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup parent) {
        Timber.e("ssss BaseMvpActivity binding() in");
        ViewDataBinding binding = null;
        if(parent == null) {
            binding = DataBindingUtil.setContentView(this, layoutId);
        } else {
            binding = DataBindingUtil.inflate(inflater, layoutId, parent, true);
        }
        binding.setLifecycleOwner(this);
        binding.setVariable(BR.viewModel, vm);
        binding.executePendingBindings();
        return binding;
    }

    protected abstract @NonNull P presenter();

    protected VM viewModel() {
        return vm;
    }

    protected V view() {
        return (V) this;
    }

    protected abstract Class<VM> viewModelClass();

    protected abstract @LayoutRes int layoutId();

    protected @IdRes int viewId() {
        return VIEW_ID_SELF;
    };

    protected ViewGroup container() {
        return null;
    }

}