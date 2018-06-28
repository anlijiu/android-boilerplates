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
package com.anlijiu.example.domain.interactor;

import com.anlijiu.example.domain.objects.Card;
import com.anlijiu.example.domain.executor.PostExecutionThread;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import com.anlijiu.example.domain.repository.CardRepository;
import io.reactivex.Observable;
import javax.inject.Inject;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving data related to an specific {@link Card}.
 */
public class GetCardDetails extends UseCase<Card, GetCardDetails.Params> {

  private final CardRepository cardRepository;

  @Inject
  GetCardDetails(CardRepository cardRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cardRepository = cardRepository;
  }

  @Override Observable<Card> buildUseCaseObservable(Params params) {
    if (params == null) {
      throw new NullPointerException();
    }
    return this.cardRepository.card(params.cardId);
  }

  public static final class Params {

    private final int cardId;

    private Params(int cardId) {
      this.cardId = cardId;
    }

    public static Params forCard(int cardId) {
      return new Params(cardId);
    }
  }
}
