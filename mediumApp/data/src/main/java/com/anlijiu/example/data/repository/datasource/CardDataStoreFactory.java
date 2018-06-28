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

import android.content.Context;
import android.support.annotation.NonNull;
import com.anlijiu.example.data.cache.CardCache;
import com.anlijiu.example.data.entity.mapper.CardEntityJsonMapper;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Factory that creates different implementations of {@link CardDataStore}.
 */
@Singleton
public class CardDataStoreFactory {

  private final Context context;
  private final CardCache userCache;

  @Inject
  CardDataStoreFactory(@NonNull Context context, @NonNull CardCache userCache) {
    this.context = context.getApplicationContext();
    this.userCache = userCache;
  }

  /**
   * Create {@link CardDataStore} from a user id.
   */
  public CardDataStore create(int userId) {
    CardDataStore cardDataStore;

    if (!this.userCache.isExpired() && this.userCache.isCached(userId)) {
      cardDataStore = new DiskCardDataStore(this.userCache);
    } else {
      cardDataStore = createRealCardDataStore();
    }

    return cardDataStore;
  }

  /**
   * Create {@link CardDataStore} to retrieve data from the real source.
   */
  public CardDataStore createRealCardDataStore() {
    return new TestCardDataStore(this.userCache);
  }
}
