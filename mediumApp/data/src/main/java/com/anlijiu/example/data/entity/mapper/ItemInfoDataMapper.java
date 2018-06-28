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
package com.anlijiu.example.data.entity.mapper;

import com.anlijiu.example.data.entity.ItemInfoEntity;
import com.anlijiu.example.domain.objects.ItemInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mapper class used to transform {@link ItemInfo} (in the domain layer) to {@link ItemInfoEntity} in the
 * data layer.
 */
@Singleton
public class ItemInfoDataMapper {

    @Inject
    ItemInfoDataMapper() {
    }

    /**
     * Transform a {@link ItemInfo} into an {@link ItemInfoEntity}.
     *
     * @param itemInfo Object to be transformed.
     * @return {@link ItemInfoEntity} if valid {@link ItemInfo} otherwise null.
     */
    public ItemInfoEntity transform(ItemInfo itemInfo) {
        ItemInfoEntity itemInfoEntity = null;
        if (itemInfo != null) {
            itemInfoEntity = new ItemInfoEntity();
            itemInfoEntity.setPrevId(itemInfo.prevId());
            itemInfoEntity.setNextId(itemInfo.nextId());
            itemInfoEntity.setTitle(itemInfo.title());
            itemInfoEntity.setIntent(itemInfo.intent());
            itemInfoEntity.setContainer(itemInfo.container());
            itemInfoEntity.setItemType(itemInfo.itemType());
            itemInfoEntity.setAppWidgetId(itemInfo.appWidgetId());
            itemInfoEntity.setIconPackage(itemInfo.iconPackage());
            itemInfoEntity.setIconResource(itemInfo.iconResource());
            itemInfoEntity.setIcon(itemInfo.icon());
            itemInfoEntity.setAppWidgetProvider(itemInfo.appWidgetProvider());
            itemInfoEntity.setModified(itemInfo.modified());
            itemInfoEntity.setRestored(itemInfo.restored());
            itemInfoEntity.setProfileId(itemInfo.profileId());
            itemInfoEntity.setRank(itemInfo.rank());
            itemInfoEntity.setOptions(itemInfo.options());
            itemInfoEntity.setScreen(itemInfo.screen());

        }
        return itemInfoEntity;
    }

    /**
     * Transform a List of {@link ItemInfo} into a Collection of {@link ItemInfoEntity}.
     *
     * @param itemInfoCollection Object Collection to be transformed.
     * @return {@link ItemInfoEntity} if valid {@link ItemInfo} otherwise null.
     */
    public List<ItemInfoEntity> transform(Collection<ItemInfo> itemInfoCollection) {
        final List<ItemInfoEntity> itemInfoEntities = new ArrayList<>(20);
        for (ItemInfo itemInfo : itemInfoCollection) {
            final ItemInfoEntity itemInfoEntity = transform(itemInfo);
            if (itemInfoEntity != null) {
                itemInfoEntities.add(itemInfoEntity);
            }
        }
        return itemInfoEntities;
    }
}
