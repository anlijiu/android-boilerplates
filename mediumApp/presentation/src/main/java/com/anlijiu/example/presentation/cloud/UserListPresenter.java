package com.anlijiu.example.presentation.cloud;

import android.support.annotation.NonNull;

import com.anlijiu.example.di.scope.ActivityScope;
import com.anlijiu.example.domain.interactor.DefaultObserver;
import com.anlijiu.example.domain.interactor.GetUserList;
import com.anlijiu.example.domain.objects.User;
import com.example.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

@ActivityScope
public class UserListPresenter extends MvpPresenter<UserListView, UserListViewModel> {
    GetUserList getUserList;

    @Inject
    public UserListPresenter(GetUserList getUserList) {
        this.getUserList = getUserList;
    }

    @Override
    public void onAttachView(@NonNull UserListView view) {

        view.loadNextPageIntent().subscribe( ignore -> getUserList.execute(new GetUserListObserver(true),  GetUserList.Params.forLastId(getViewModel().lastUserId())));
        view.pullToRefreshIntent().subscribe( ignore -> getUserList.execute(new GetUserListObserver(), GetUserList.Params.create(1, 25, true)));
        getUserList.execute(new GetUserListObserver(), GetUserList.Params.create(1, 25, false));

        Timber.e("UserListPresenter onAttachView");
    }

    @Override
    public void onAttachViewModel(UserListViewModel viewModel) {

    }

    @Override
    public void onDetachView() {
        Timber.e("UserListPresenter onDetachView");
    }

    @Override
    public void onDetachViewModel() {

    }

    @Override
    public void destroy() {
        super.destroy();
        Timber.e("UserListPresenter destroy");
    }

    private final class GetUserListObserver extends DefaultObserver<List<User>> {
        boolean isAdd = false;

        public GetUserListObserver() {}

        public GetUserListObserver(boolean isAdd) {
            this.isAdd = isAdd;
        }
        @Override
        protected void onStart() {
            ifViewModelAttached((viewModel) -> {
                if(!isAdd) {
                    viewModel.refreshing.setValue(true);
                }
                viewModel.loading.setValue(true);
            });
        }

        @Override
        public void onNext(List<User> list) {
            ifViewModelAttached( (viewModel) -> {
                if(isAdd) {
                    viewModel.addItems(list);
                } else {
                    viewModel.updateItems(list);
                }
            } );
        }
    }
}
