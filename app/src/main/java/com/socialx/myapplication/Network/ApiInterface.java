package com.socialx.myapplication.Network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiInterface {
    @GET(ApiConstants.GET_ARTICLES)
    Call<ResponseBody> getArticles(@QueryMap Map<String,String>setFilter);
}
