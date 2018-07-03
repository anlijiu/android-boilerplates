package com.anlijiu.example.data.cloud.user;

import com.anlijiu.example.domain.objects.Repo;
import com.anlijiu.example.domain.objects.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

public interface UserCacheProviders {
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES) Observable<Reply<List<Repo>>> getRepos(Observable<List<Repo>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<User>>> getUsers(Observable<List<User>> oUsers, DynamicKey idLastUserQueried, EvictProvider evictProvider);


}
