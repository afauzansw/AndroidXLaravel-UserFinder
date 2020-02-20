package com.santridev.userandro.apiservice;

import com.santridev.userandro.model.ResponseRepos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BaseApiService {

    @GET("user/search/{username}")
    Observable<List<ResponseRepos>> requestRepos(@Path("username") String username);
}
