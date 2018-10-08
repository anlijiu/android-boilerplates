package com.anlijiu.example.presentation.appwidget;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.anlijiu.example.data.compat.AppWidgetManagerCompat;
import com.anlijiu.example.di.scope.ActivityScope;
import com.anlijiu.example.domain.interactor.DefaultObserver;
import com.anlijiu.example.domain.interactor.GetItemInfoList;
import com.anlijiu.example.domain.interactor.GetSimpleList;
import com.anlijiu.example.domain.interactor.RemoveInvalidItemInfos;
import com.anlijiu.example.domain.interactor.UpdateSimpleList;
import com.anlijiu.example.domain.objects.ItemInfo;
import com.anlijiu.example.domain.objects.ItemType;
import com.anlijiu.example.domain.objects.SimpleItem;
import com.example.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

@ActivityScope
public class WidgetListPresenter extends MvpPresenter<WidgetListView, WidgetListViewModel> {

    GetItemInfoList getItemInfoList;

    @Inject
    AppWidgetManagerCompat appWidgetManagerCompat;
    @Inject
    RemoveInvalidItemInfos removeInvalidItemInfosUseCase;
    @Inject
    LauncherAppWidgetHost appWidgetHost;

    @Inject
    Context context;

    @Inject
    public WidgetListPresenter(GetItemInfoList getItemInfoList) {
        this.getItemInfoList = getItemInfoList;
        getItemInfoList.execute(new GetItemInfoListObserver(), GetItemInfoList.Params.forItemTypes(new int[]{ItemType.ITEM_TYPE_WIDGET.value()}));
    }

    @Override
    public void onAttachView(@NonNull WidgetListView view) {

    }

    @Override
    public void onAttachViewModel(WidgetListViewModel viewModel) {

    }

    @Override
    public void onDetachView() {

    }

    @Override
    public void onDetachViewModel() {

    }

    @Override
    public void destroy() {
        super.destroy();

    }

    private void showWidgets(List<ItemInfo> itemInfos) {
        Timber.d("show Widgets  itemInfos size is %d", itemInfos.size());
        ifViewModelAttached(viewModel -> {
            Timber.d("show Widgets  ifViewModelAttached in !!!! itemInfos size is %d", itemInfos.size());

            List<WidgetModel> models = new ArrayList<>();
            for(ItemInfo itemInfo : itemInfos) {
                if(itemInfo.appWidgetId() != -1) {
                    AppWidgetProviderInfo info = appWidgetManagerCompat.getAppWidgetInfo(itemInfo.appWidgetId());
                    if(info == null) {
                        removeInvalidItemInfosUseCase.execute(new DefaultObserver<>(), RemoveInvalidItemInfos.Params.forIds(itemInfo.id()));
                        continue;
                    }
                    View view = appWidgetHost.createView(context, itemInfo.appWidgetId(), info);
                    view.setPadding(0, 0, 0, 0);
                    final WidgetModel model = WidgetModel.builder().id(itemInfo.id()).widgetView(view).build();
                    models.add(model);
                }
            }
            viewModel.updateItems(models);
        });
    }


    private final class GetItemInfoListObserver extends DefaultObserver<List<ItemInfo>> {
        @Override
        public void onNext(List<ItemInfo> itemInfos) {
            super.onNext(itemInfos);
            showWidgets(itemInfos);
        }
    }
}
