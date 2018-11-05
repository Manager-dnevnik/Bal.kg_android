package com.alanaandnazar.qrscanner.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Erlan on 24.08.2017.
 */

class RetrofitClient {
    private static Retrofit retrofit = null;


    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                   // .addHeader("Authorization", "Token 6fd9bd91e2d1823a9eaa34ff852e9b36d25b633f")
                    .build();
            return chain.proceed(request);
        }
    }).build();
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    // RxJava
                    //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())/
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(client)
                    .build();
        }
        return retrofit;
    }

}
