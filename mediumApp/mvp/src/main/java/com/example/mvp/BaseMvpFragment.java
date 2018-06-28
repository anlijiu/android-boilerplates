package com.example.mvp;

import android.arch.lifecycle.ViewModel;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;


public abstract class BaseMvpFragment<V extends IMvpView, P extends MvpPresenter<V, VM>, VM extends ViewModel>
        extends DaggerFragment implements IMvpView {
    public static final int VIEW_ID_SELF = 0;
    P presenter;
    V baseView;
    VM viewModel;
    Unbinder unbinder;
    protected ViewDataBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        if(viewId() != VIEW_ID_SELF) {
            baseView = (V) ButterKnife.findById(view, viewId());
        } else {
            baseView = (V) this;
        }

        presenter = presenter();
        presenter.attachView(baseView, viewModel);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        binding = binding(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    protected ViewDataBinding binding(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        return binding;
    }

    /**
     * @return mvp中的view的id， 如果view是fragment本身，则返回VIEW_ID_SELF
     */
    protected @IdRes int viewId() {
        return VIEW_ID_SELF;
    }

    protected abstract P presenter();

    public abstract int layoutId();

}
