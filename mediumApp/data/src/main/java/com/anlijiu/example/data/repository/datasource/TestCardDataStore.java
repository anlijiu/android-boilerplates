package com.anlijiu.example.data.repository.datasource;


import com.anlijiu.example.data.cache.CardCache;
import com.anlijiu.example.data.entity.CardEntity;
import com.anlijiu.example.domain.objects.Card;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by anlijiu on 18-3-21.
 */

public class TestCardDataStore implements CardDataStore {
    List<CardEntity> list = new ArrayList<>();

    TestCardDataStore(CardCache cardCache) {
        for(int i = 1; i < 20; ++i) {
            CardEntity cardEntity = new CardEntity();
            cardEntity.setId(i);
            cardEntity.setCoverImageUrl("url - "+i);
            cardEntity.setTargetUrl("targetUrl - "+i);
            cardEntity.setTitle("title - "+i);
            list.add(cardEntity);
        }
    }

    @Override
    public Observable<List<CardEntity>> cardEntityList() {
        return Observable.create(emitter -> {
            emitter.onNext(list);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<CardEntity> cardEntityDetails(int cardId) {
        final boolean[] found = {false};
        return Observable.create(emitter -> {
            for(CardEntity entity : list) {
                if(entity.getId() == cardId) {
                    found[0] = true;
                    emitter.onNext(entity);
                    emitter.onComplete();
                }
            }
            if(!found[0]) {
                emitter.onNext(null);
                emitter.onComplete();
            }
        });
    }
}
