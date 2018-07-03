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
    public void onAttachView(@NonNull UserListView view, UserListViewModel viewModel) {

        getUserList.execute(new DefaultObserver<List<User>>() {
            @Override
            public void onNext(List<User> list) {
                Timber.d("ssss list size is %d", list.size());
                ifViewModelAttached(viewModel -> viewModel.updateItems(list));
            }

            @Override
            public void onError(Throwable exception) {
                super.onError(exception);
                Timber.d("ssss, getUserList error %s", exception.toString());
                exception.printStackTrace();
            }
        }, GetUserList.Params.create(1, 25, true));

        // Disposable increaseClickStream = view.increaseCount().subscribe(o -> ifViewAttached((view1, viewModel1) -> {
        //     updateUserList.execute(new UpdateUserListObserver(), UpdateUserList.Params.add());
        //     viewModel1.textValue.setValue("value is " + viewModel1.increase());
        // }));
        Timber.e("UserListPresenter onAttachView");
    }

    @Override
    public void onDetachView() {
        Timber.e("UserListPresenter onDetachView");
    }

    @Override
    public void destroy() {
        super.destroy();
        Timber.e("UserListPresenter destroy");
    }

    private final class UpdateUserListObserver extends DefaultObserver<List<User>> {
        @Override
        public void onNext(List<User> list) {
            ifViewAttached( (view, viewModel) -> viewModel.updateItems(list));
        }
    }
}
