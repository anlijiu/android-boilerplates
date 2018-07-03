package com.anlijiu.example.data.repository;

import com.anlijiu.example.data.cloud.user.UserApi;
import com.anlijiu.example.data.cloud.user.UserCacheProviders;
import com.anlijiu.example.domain.objects.Repo;
import com.anlijiu.example.domain.objects.User;
import com.anlijiu.example.domain.repository.CloudRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

@Singleton
public class CloudDataRepository implements CloudRepository {
    @Inject
    UserCacheProviders userCacheProviders;

    @Inject UserApi userApi;

    @Inject
    public CloudDataRepository() {}

    @Override
    public Observable<List<User>> getUsers(int idLastUserQueried, int perPage, boolean update) {
        return userCacheProviders.getUsers(userApi.getUsers(idLastUserQueried, perPage), new DynamicKey(idLastUserQueried), new EvictDynamicKey(update)).map(listReply -> listReply.getData());
    }

    @Override
    public Observable<List<Repo>> getRepos(String userName, boolean update) {
        return userCacheProviders.getRepos(userApi.getRepos(userName), new DynamicKey(userName), new EvictDynamicKey(update)).map(listReply -> listReply.getData());
    }
}
