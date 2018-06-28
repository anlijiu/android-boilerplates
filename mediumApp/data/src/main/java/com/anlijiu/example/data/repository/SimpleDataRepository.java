package com.anlijiu.example.data.repository;

import com.anlijiu.example.domain.objects.SimpleItem;
import com.anlijiu.example.domain.repository.SimpleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

@Singleton
public class SimpleDataRepository implements SimpleRepository {
    List<SimpleItem> list = new ArrayList<>();

    @Inject
    public SimpleDataRepository() {
        list.add(SimpleItem.builder().id(1).name("haha").build());
        list.add(SimpleItem.builder().id(2).name("hehe").build());
        list.add(SimpleItem.builder().id(3).name("hoho").build());
    }

    @Override
    public Observable<List<SimpleItem>> simpleItems() {
        return Observable.just(list);
    }

    @Override
    public Observable<List<SimpleItem>> addRandomItem() {
        list.add(SimpleItem.builder().id(list.size()+2).name(UUID.randomUUID().toString()).build());
        return Observable.just(list);
    }

    @Override
    public Observable<List<SimpleItem>> removeRandomItem() {
        list.remove(0);
        return Observable.just(list);
    }
}
