/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anlijiu.example.data.repository;

import com.anlijiu.example.data.entity.mapper.CardEntityDataMapper;
import com.anlijiu.example.data.repository.datasource.CardDataStore;
import com.anlijiu.example.data.repository.datasource.CardDataStoreFactory;
import com.anlijiu.example.domain.objects.Card;
import com.anlijiu.example.domain.repository.CardRepository;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link CardRepository} for retrieving card data.
 */
@Singleton
public class CardDataRepository implements CardRepository {

  private final CardDataStoreFactory cardDataStoreFactory;
  private final CardEntityDataMapper cardEntityDataMapper;

  /**
   * Constructs a {@link CardRepository}.
   *
   * @param dataStoreFactory A factory to construct different data source implementations.
   * @param cardEntityDataMapper {@link CardEntityDataMapper}.
   */
  @Inject
  CardDataRepository(CardDataStoreFactory dataStoreFactory,
      CardEntityDataMapper cardEntityDataMapper) {
    this.cardDataStoreFactory = dataStoreFactory;
    this.cardEntityDataMapper = cardEntityDataMapper;
  }

  @Override public Observable<List<Card>> cards() {
    //we always get all cards from the cloud
    final CardDataStore cardDataStore = this.cardDataStoreFactory.createRealCardDataStore();
    return cardDataStore.cardEntityList().map(this.cardEntityDataMapper::transform);
  }

  @Override public Observable<Card> card(int cardId) {
    final CardDataStore cardDataStore = this.cardDataStoreFactory.create(cardId);
    return cardDataStore.cardEntityDetails(cardId).map(this.cardEntityDataMapper::transform);
  }
}
