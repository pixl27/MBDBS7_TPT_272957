package com.example.myapplication.service;

import com.example.myapplication.model.Match;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.Observable;

public class MatchService {
    private MatchAPI matchAPI;

    public MatchService() {
        this.matchAPI = BaseAPI.getClient().create(MatchAPI.class);
    }
    public void getMatchs() {
        this.matchAPI.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Match>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // Do something useful
                    }

                    @Override
                    public void onSuccess(List<Match> toDos) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // Do something very useful
                    }
                });
    }
}
