package com.anlijiu.example.domain.repository;

import com.anlijiu.example.domain.objects.SimpleItem;

import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface SimpleRepository {

    Observable<List<SimpleItem>> simpleItems();

    Observable<List<SimpleItem>> addRandomItem();

    Observable<List<SimpleItem>> removeRandomItem();
}
