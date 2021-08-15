package com.example.myapplication.service;

import com.example.myapplication.model.Match;
import io.reactivex.Observable;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MatchAPI {
    @GET("/getallmatchtest")
    Single<List<Match>> findAll();
}
