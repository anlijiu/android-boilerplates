/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
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
 * Mapper class used to transform {@link ItemInfoEntity} (in the data layer) to {@link ItemInfo} in the
 * domain layer.
 */
@Singleton
public class ItemInfoEntityDataMapper {

  @Inject
  ItemInfoEntityDataMapper() {}

  /**
   * Transform a {@link ItemInfoEntity} into an {@link ItemInfo}.
   *
   * @param shortcutEntity Object to be transformed.
   * @return {@link ItemInfo} if valid {@link ItemInfoEntity} otherwise null.
   */
  public ItemInfo transform(ItemInfoEntity shortcutEntity) {
    ItemInfo shortcut = null;
    if (shortcutEntity != null) {
      shortcut = ItemInfo.builder()
              .id(shortcutEntity.getId())
              .prevId(shortcutEntity.getPrevId())
              .nextId(shortcutEntity.getNextId())
              .appWidgetId(shortcutEntity.getAppWidgetId())
              .appWidgetProvider(shortcutEntity.getAppWidgetProvider())
              .container(shortcutEntity.getContainer())
              .icon(shortcutEntity.getIcon())
              .iconPackage(shortcutEntity.getIconPackage())
              .iconResource(shortcutEntity.getIconResource())
              .intent(shortcutEntity.getIntent())
              .itemType(shortcutEntity.getItemType())
              .modified(shortcutEntity.getModified())
              .options(shortcutEntity.getOptions())
              .profileId(shortcutEntity.getProfileId())
              .rank(shortcutEntity.getRank())
              .restored(shortcutEntity.getRestored())
              .screen(shortcutEntity.getScreen())
              .title(shortcutEntity.getTitle())
              .build();
    }
    return shortcut;
  }

  /**
   * Transform a List of {@link ItemInfoEntity} into a Collection of {@link ItemInfo}.
   *
   * @param shortcutEntityCollection Object Collection to be transformed.
   * @return {@link ItemInfo} if valid {@link ItemInfoEntity} otherwise null.
   */
  public List<ItemInfo> transform(Collection<ItemInfoEntity> shortcutEntityCollection) {
    final List<ItemInfo> shortcutList = new ArrayList<>(20);
    for (ItemInfoEntity shortcutEntity : shortcutEntityCollection) {
      final ItemInfo shortcut = transform(shortcutEntity);
      if (shortcut != null) {
        shortcutList.add(shortcut);
      }
    }
    return shortcutList;
  }
}
