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

import com.anlijiu.example.data.entity.CardEntity;
import com.anlijiu.example.domain.objects.Card;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mapper class used to transform {@link CardEntity} (in the data layer) to {@link Card} in the
 * domain layer.
 */
@Singleton
public class CardEntityDataMapper {

  @Inject
  CardEntityDataMapper() {}

  /**
   * Transform a {@link CardEntity} into an {@link Card}.
   *
   * @param cardEntity Object to be transformed.
   * @return {@link Card} if valid {@link CardEntity} otherwise null.
   */
  public Card transform(CardEntity cardEntity) {
    Card card = null;
    if (cardEntity != null) {
      card = Card.builder()
              .id(cardEntity.getId())
              .coverImageUrl(cardEntity.getCoverImageUrl())
              .targetUrl(cardEntity.getTargetUrl())
              .title(cardEntity.getTitle())
              .build();
    }
    return card;
  }

  /**
   * Transform a List of {@link CardEntity} into a Collection of {@link Card}.
   *
   * @param cardEntityCollection Object Collection to be transformed.
   * @return {@link Card} if valid {@link CardEntity} otherwise null.
   */
  public List<Card> transform(Collection<CardEntity> cardEntityCollection) {
    final List<Card> cardList = new ArrayList<>(20);
    for (CardEntity cardEntity : cardEntityCollection) {
      final Card card = transform(cardEntity);
      if (card != null) {
        cardList.add(card);
      }
    }
    return cardList;
  }
}
