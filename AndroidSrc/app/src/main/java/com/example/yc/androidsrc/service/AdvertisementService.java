package com.example.yc.androidsrc.service;

import com.example.yc.androidsrc.model.Advertisement;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface AdvertisementService {
    @GET("version")
    Observable<List<Advertisement>> getAdvertisementList();
}
