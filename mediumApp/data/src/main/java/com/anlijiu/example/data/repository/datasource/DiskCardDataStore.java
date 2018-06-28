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
package com.anlijiu.example.data.repository.datasource;

import com.anlijiu.example.data.cache.CardCache;
import com.anlijiu.example.data.entity.CardEntity;
import io.reactivex.Observable;
import java.util.List;

/**
 * {@link CardDataStore} implementation based on file system data store.
 */
class DiskCardDataStore implements CardDataStore {

  private final CardCache cardCache;

  /**
   * Construct a {@link CardDataStore} based file system data store.
   *
   * @param cardCache A {@link CardCache} to cache data retrieved from the api.
   */
  DiskCardDataStore(CardCache cardCache) {
    this.cardCache = cardCache;
  }

  @Override public Observable<List<CardEntity>> cardEntityList() {
    //TODO: implement simple cache for storing/retrieving collections of cards.
    throw new UnsupportedOperationException("Operation is not available!!!");
  }

  @Override public Observable<CardEntity> cardEntityDetails(final int cardId) {
     return this.cardCache.get(cardId);
  }
}
