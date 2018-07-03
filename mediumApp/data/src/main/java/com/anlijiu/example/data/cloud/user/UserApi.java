package com.anlijiu.example.data.cloud.user;

import com.anlijiu.example.domain.objects.Repo;
import com.anlijiu.example.domain.objects.User;

import io.reactivex.Observable;
import java.util.List;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserApi {

    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers({HEADER_API_VERSION})
    @GET("/users") Observable<List<User>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);

    @Headers({HEADER_API_VERSION})
    @GET("/users/{username}/repos")
    Observable<List<Repo>> getRepos(@Path("username") String userName);

    @Headers({HEADER_API_VERSION})
    @GET("/users/{username}") Observable<Response<User>> getUser(@Path("username") String username);
}
