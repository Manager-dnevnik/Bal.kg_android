package com.alanaandnazar.qrscanner.retrofit;

import android.app.Application;


public class App extends Application {


    private App() {}

    public static String BASE_URL ="https://bal.kg/api/";

    public static BalAPI getApi() {

        return RetrofitClient.getClient(BASE_URL).create(BalAPI.class);
        /* RetrofitHolder.getRetrofit().create(BalAPI.class);*/
    }

}
