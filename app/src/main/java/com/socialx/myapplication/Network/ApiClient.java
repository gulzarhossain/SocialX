package com.socialx.myapplication.Network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.socialx.myapplication.utility.AppPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private static Context mContext;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .client(buildOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(loggingInterceptor);
        httpClientBuilder.addNetworkInterceptor(
                new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = null;
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder();
                        request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                }).build();
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }

    public static ApiInterface getApiInterFace(Context context) {
        mContext=context;
        Retrofit retrofit = ApiClient.getClient();
        return retrofit.create(ApiInterface.class);
    }

    public static <T> T getPayload(Class<T> payloadClass, String jsonData) {
        return new Gson().fromJson(jsonData, payloadClass);
    }

    public static void callApi(Call<ResponseBody> apiCall, ApiResponse apiResponse, int apiRequest) {
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        apiResponse.OnResponse(response.body().string(), apiRequest);
                    } catch (Exception e) {
                        Log.e("isSuccessful Exception", e.getMessage());
                    }
                } else {
                    try {
                        apiResponse.OnError(response.errorBody().string(), apiRequest);
                    } catch (Exception e) {
                        Log.e("notSuccessful Exception", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                JSONObject object = new JSONObject();
                try {
                    object.put("status", 0);
                    object.put("message", t.getMessage());
                } catch (JSONException e) {
                }
                apiResponse.OnError(object.toString(), apiRequest);
            }
        });
    }
}