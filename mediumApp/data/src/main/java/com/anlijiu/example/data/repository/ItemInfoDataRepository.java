/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anlijiu.example.data.repository;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetProviderInfo;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import com.anlijiu.example.data.compat.AppWidgetManagerCompat;
import com.anlijiu.example.data.compat.LauncherAppsCompat;
import com.anlijiu.example.data.compat.PackageUserKey;
import com.anlijiu.example.data.compat.ShortcutInfoCompat;
import com.anlijiu.example.data.entity.ItemInfoEntity;
import com.anlijiu.example.data.entity.ItemInfoEntity_;

import com.anlijiu.example.data.entity.mapper.CardEntityDataMapper;
import com.anlijiu.example.data.entity.mapper.ItemInfoDataMapper;
import com.anlijiu.example.data.entity.mapper.ItemInfoEntityDataMapper;
import com.anlijiu.example.data.repository.datasource.CardDataStore;
import com.anlijiu.example.data.repository.datasource.CardDataStoreFactory;
import com.anlijiu.example.domain.objects.ItemType;
import com.anlijiu.example.domain.interactor.DefaultObserver;
import com.anlijiu.example.domain.objects.Card;
import com.anlijiu.example.domain.objects.ItemInfo;
import com.anlijiu.example.domain.objects.ItemSortData;
import com.anlijiu.example.domain.repository.CardRepository;
import com.anlijiu.example.domain.repository.ItemInfoRepository;
import com.anlijiu.example.utils.rx.RetryWithTime;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.OrderFlags;
import io.objectbox.query.Query;
import io.objectbox.query.QueryFilter;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscription;
import io.objectbox.rx.RxBoxStore;
import io.objectbox.rx.RxQuery;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link ItemInfoRepository} for retrieving card data.
 */
@Singleton
public class ItemInfoDataRepository implements ItemInfoRepository, LauncherAppsCompat.OnAppsChangedCallbackCompat {
    private final Box<ItemInfoEntity> itemInfoEntityBox;

    @Inject
    ItemInfoEntityDataMapper itemInfoEntityDataMapper;

    @Inject
    LauncherAppsCompat launcherAppsCompat;

    private ItemInfoDataMapper itemInfoDataMapper;
    private AppWidgetManagerCompat appWidgetManagerCompat;
    private AppWidgetHost appWidgetHost;
    /**
     * Constructs a {@link CardRepository}.
     *
     * @param boxStore {@link BoxStore}.
     */
    @Inject
    ItemInfoDataRepository(BoxStore boxStore,
                           AppWidgetManagerCompat appWidgetManagerCompat,
                           AppWidgetHost appWidgetHost,
                           ItemInfoDataMapper itemInfoDataMapper,
                           LauncherAppsCompat launcherAppsCompat) {
        this.itemInfoEntityBox = boxStore.boxFor(ItemInfoEntity.class);
        this.launcherAppsCompat = launcherAppsCompat;
        this.appWidgetManagerCompat = appWidgetManagerCompat;
        this.appWidgetHost = appWidgetHost;
        this.itemInfoDataMapper = itemInfoDataMapper;
        launcherAppsCompat.addOnAppsChangedCallback(this);

        initDataForTheFirstTime();
    }

    private void initDataForTheFirstTime() {
        long widgetCount = itemInfoEntityBox.query().equal(ItemInfoEntity_.itemType, ItemType.ITEM_TYPE_WIDGET.value()).build().count();
        if(widgetCount == 0) {
            addWidgetsByPackageUser(null);
        }
    }

    private boolean addWidgetsByPackageUser(@Nullable PackageUserKey packageUserKey) {
        boolean isSuccess = false;

        List<ItemInfo> itemInfos = new ArrayList<>();

        List<AppWidgetProviderInfo> infos = appWidgetManagerCompat.getAllProviders(packageUserKey);

        Timber.d("ssss addWidgetsByPackageUser getAllProviders %s  size is %d", packageUserKey, infos.size());
        for (AppWidgetProviderInfo info : infos) {
            int appWidgetId = appWidgetHost.allocateAppWidgetId();
            isSuccess = appWidgetManagerCompat.bindAppWidgetIdIfAllowed(appWidgetId, info, null);
            if(isSuccess) {
                Timber.d("ssss addWidgetsByPackageUser bindAppWidgetIdIfAllowed success ");
                ItemInfo itemInfo = ItemInfo.builder().appWidgetId(appWidgetId).appWidgetProvider(info.provider.flattenToString()).itemType(ItemType.ITEM_TYPE_WIDGET.value()).build();
                itemInfos.add(itemInfo);
            } else {
                Timber.d("ssss addWidgetsByPackageUser bindAppWidgetIdIfAllowed failed ");
            }
        }
        Timber.d("ssss addWidgetsByPackageUser widgets size  is %d", itemInfos.size());
        itemInfoEntityBox.put(this.itemInfoDataMapper.transform(itemInfos));
        Timber.d("ssss addWidgetsByPackageUser end ");
        return isSuccess;
    }

    public List<ItemInfo> sortItemInfo(List<ItemInfo> itemInfos) {
        if (null == itemInfos || itemInfos.isEmpty()) {
            throw new IllegalArgumentException("itemInfo is invalid");
        }

        List<ItemInfo> storeItemList = new LinkedList<>();
        storeItemList.add(itemInfos.remove(0));

        boolean isFirstAdd = true;
        while (isFirstAdd && !itemInfos.isEmpty()) {
            isFirstAdd = false;
            ItemInfo firstItemInfo = storeItemList.get(0);
            if (ItemSortData.INVALID_ID != firstItemInfo.prevId()
                    && ItemSortData.DEFAULT_ID != firstItemInfo.prevId()) {
                for (ItemInfo itemInfo : itemInfos) {
                    if (firstItemInfo.prevId() == itemInfo.id()) {
                        isFirstAdd = true;
                        storeItemList.add(0, itemInfo);
                        itemInfos.remove(itemInfo);
                        break;
                    }
                }
            }
        }

        boolean isLastAdd = true;
        while (isLastAdd && !itemInfos.isEmpty()) {
            isLastAdd = false;
            ItemInfo lastItemInfo = storeItemList.get(storeItemList.size() - 1);

            if (ItemSortData.INVALID_ID != lastItemInfo.nextId()) {
                if (ItemSortData.DEFAULT_ID != lastItemInfo.nextId()) {
                    for (ItemInfo itemInfo : itemInfos) {
                        if (lastItemInfo.nextId() == itemInfo.id()) {
                            isLastAdd = true;
                            storeItemList.add(storeItemList.size(), itemInfo);
                            itemInfos.remove(itemInfo);
                            break;
                        }
                    }
                } else {
                    isLastAdd = true;
                    storeItemList.add(itemInfos.remove(0));
                }
            }
        }

        return storeItemList;
    }

    @Override
    public Observable<List<ItemInfo>> itemInfos(int[] itemTypes) {
        //we always get all cards from the cloud
        Query<ItemInfoEntity> query = itemInfoEntityBox.query().in(ItemInfoEntity_.itemType, itemTypes).build();

        return RxQuery.observable(query).map(itemInfoEntities -> sortItemInfo(itemInfoEntityDataMapper.transform(itemInfoEntities)));
    }

    @Override
    public Observable<Void> insertItemInfo(ItemInfo itemInfo) {
        return Observable.create(emitter -> {
            itemInfoEntityBox.put(this.itemInfoDataMapper.transform(itemInfo));
            emitter.onNext(null);
            emitter.onComplete();
        });
//        itemInfoEntityBox.put(this.itemInfoDataMapper.transform(itemInfo));
    }

    @Override
    public Observable<Void> insertItemInfos(Collection<ItemInfo> itemInfos) {
        return Observable.create(emitter -> {
            List<ItemInfoEntity> list = this.itemInfoDataMapper.transform(itemInfos);
            Timber.d(" ssss insertItemInfos list size is %d", list.size());
            itemInfoEntityBox.put(this.itemInfoDataMapper.transform(itemInfos));
            emitter.onNext(null);
            emitter.onComplete();
        });
//        itemInfoEntityBox.put(this.itemInfoDataMapper.transform(itemInfos));
    }

    @Override
    public Observable<Void> removeItemInfos(long[] ids) {
        return Observable.create(emitter -> {
            itemInfoEntityBox.remove(ids);
            emitter.onNext(null);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Void> moveItemInfo(Collection<ItemSortData> sortDatas) {
        return Observable.create(emitter -> {
            long[] idValues = new long[sortDatas.size()];
            List<ItemSortData> idLists = (List<ItemSortData>) sortDatas;
            for (int i = 0; i < idLists.size(); i++) {
                idValues[i] = idLists.get(i).id();
            }

            Query<ItemInfoEntity> query = itemInfoEntityBox.query().in(ItemInfoEntity_.id, idValues).build();
            List<ItemInfoEntity> infoEntities = query.find();
            for (ItemInfoEntity itemInfoEntity :infoEntities) {
                for (ItemSortData itemSortData : sortDatas) {
                    if (itemSortData.id() == itemInfoEntity.getId()) {
                        itemInfoEntity.setPrevId(itemSortData.prevId());
                        itemInfoEntity.setNextId(itemSortData.nextId());
                        break;
                    }
                }
            }
            itemInfoEntityBox.put(infoEntities);

            emitter.onNext(null);
            emitter.onComplete();
        });
    }

    @Override
    public void onPackageRemoved(String packageName, UserHandle user) {
        Timber.d("ItemInfoDataRepository -- onPackageRemoved in, packageName:%s", packageName);
        itemInfoEntityBox.query().contains(ItemInfoEntity_.appWidgetProvider, packageName+"/").build().remove();
    }

    DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedTime( FormatStyle.FULL )
                    .withLocale( Locale.CHINA )
                    .withZone( ZoneId.systemDefault() );

    @Override
    public void onPackageAdded(String packageName, UserHandle user) {
        Timber.d("ssss ItemInfoDataRepository -- onPackageAdded in, packageName:%s, time is %s", packageName, formatter.format(Instant.now()));

        Observable.create((ObservableOnSubscribe<String>)emitter -> {
            Timber.d("ssss  onPackageAdded , observable create in time is %s" , formatter.format(Instant.now()));
            if(addWidgetsByPackageUser(new PackageUserKey(packageName, user))) {
                emitter.onNext("");
                emitter.onComplete();
            } else {
                emitter.onError(new Throwable());
            }

        }).retryWhen(new RetryWithTime()).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void onPackageChanged(String packageName, UserHandle user) {

    }

    @Override
    public void onPackagesAvailable(String[] packageNames, UserHandle user, boolean replacing) {

    }

    @Override
    public void onPackagesUnavailable(String[] packageNames, UserHandle user, boolean replacing) {

    }

    @Override
    public void onPackagesSuspended(String[] packageNames, UserHandle user) {

    }

    @Override
    public void onPackagesUnsuspended(String[] packageNames, UserHandle user) {

    }

    @Override
    public void onShortcutsChanged(String packageName, List<ShortcutInfoCompat> shortcuts, UserHandle user) {

    }
}
