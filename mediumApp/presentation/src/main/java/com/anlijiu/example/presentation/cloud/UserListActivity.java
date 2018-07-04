package com.anlijiu.example.presentation.cloud;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mvp.BaseMvpActivity;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import com.anlijiu.example.R;

public class UserListActivity extends BaseMvpActivity<UserListView, UserListPresenter, UserListViewModel> implements UserListView {

    @Inject
    UserListPresenter presenter;

    @BindView(R.id.pb_loading)
    MaterialProgressBar pbLoading;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

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

    @Override public Observable<Boolean> loadNextPageIntent() {
        return RxRecyclerView.scrollStateChanges(recyclerView)
                .filter(event -> !viewModel().loading.getValue())
                .filter(event -> event == RecyclerView.SCROLL_STATE_IDLE)
                .filter(event -> ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition()
                        == viewModel().adapter.getItems().size() - 1)
                .map(integer -> true);
    }

    @Override public Observable<Boolean> pullToRefreshIntent() {
        return RxSwipeRefreshLayout.refreshes(swipeRefreshLayout).map(ignored -> true);
    }

}
