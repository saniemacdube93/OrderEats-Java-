package com.example.androideatitjava.Remote;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICloudFunctions {
    @GET("getCustomToken")
    Observable<ResponseBody> getCustomtToken(@Query("access_token") String accessToken);




}
