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
package com.anlijiu.example.domain.repository;

import com.anlijiu.example.domain.objects.ItemInfo;
import com.anlijiu.example.domain.objects.ItemSortData;

import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;

/**
 * Interface that represents a Repository for getting {@link ItemInfo} related data.
 */
public interface ItemInfoRepository {

    /**
     * Get an {@link Observable} which will emit a List of {@link ItemInfo}.
     */
    Observable<List<ItemInfo>> itemInfos(int[] itemTypes);

    Observable<Void> insertItemInfo(ItemInfo itemInfo);

    Observable<Void> insertItemInfos(Collection<ItemInfo> itemInfos);

    Observable<Void> removeItemInfos(long[] ids);

    Observable<Void> moveItemInfo(Collection<ItemSortData> sortDatas);
}
