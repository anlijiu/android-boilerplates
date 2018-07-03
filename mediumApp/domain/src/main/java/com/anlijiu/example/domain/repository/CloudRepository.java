package com.anlijiu.example.domain.repository;

import com.anlijiu.example.domain.objects.Repo;
import com.anlijiu.example.domain.objects.User;

import java.util.List;

import io.reactivex.Observable;

public interface CloudRepository {
    Observable<List<User>> getUsers(int idLastUserQueried, int perPage, final boolean update);
    Observable<List<Repo>> getRepos(final String userName, final boolean update);
}
