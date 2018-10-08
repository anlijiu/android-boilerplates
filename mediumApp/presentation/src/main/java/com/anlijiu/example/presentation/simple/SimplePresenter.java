package com.anlijiu.example.presentation.simple;

import android.support.annotation.NonNull;

import com.anlijiu.example.di.scope.ActivityScope;
import com.anlijiu.example.domain.interactor.DefaultObserver;
import com.anlijiu.example.domain.interactor.GetSimpleList;
import com.anlijiu.example.domain.interactor.UpdateSimpleList;
import com.anlijiu.example.domain.objects.SimpleItem;
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
public class SimplePresenter extends MvpPresenter<SimpleView, SimpleViewModel> {
    CompositeDisposable compositeDisposable;
    GetSimpleList getSimpleList;
    @Inject
    UpdateSimpleList updateSimpleList;

    @Inject
    public SimplePresenter(GetSimpleList getSimpleList) {
        this.getSimpleList = getSimpleList;
        compositeDisposable = new CompositeDisposable();

    }

    @Override
    public void onAttachView(@NonNull SimpleView view) {

        getSimpleList.execute(new DefaultObserver<List<SimpleItem>>() {
            @Override
            public void onNext(List<SimpleItem> list) {
                Timber.d("ssss list size is %d", list.size());
                ifViewModelAttached(viewModel -> viewModel.updateItems(list));
            }
        }, null);

        Disposable increaseClickStream = view.increaseCount().subscribe(o -> ifViewModelAttached((viewModel1) -> {
            updateSimpleList.execute(new UpdateSimpleListObserver(), UpdateSimpleList.Params.add());
            viewModel1.textValue.setValue("value is " + viewModel1.increase());
        }));
        compositeDisposable.add(increaseClickStream);
        Timber.e("SimplePresenter onAttachView");
    }

    @Override
    public void onAttachViewModel(SimpleViewModel viewModel) {

    }

    @Override
    public void onDetachView() {
        Timber.e("SimplePresenter onDetachView");
        compositeDisposable.dispose();
    }

    @Override
    public void onDetachViewModel() {

    }

    @Override
    public void destroy() {
        super.destroy();
        Timber.e("SimplePresenter destroy");
    }

    private final class UpdateSimpleListObserver extends DefaultObserver<List<SimpleItem>> {
        @Override
        public void onNext(List<SimpleItem> list) {
            ifViewModelAttached( (viewModel) -> viewModel.updateItems(list));
        }
    }
}
