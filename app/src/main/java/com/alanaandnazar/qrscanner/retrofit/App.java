package com.alanaandnazar.qrscanner.retrofit;

import android.app.Application;


public class App extends Application {


    private App() {}

    public static String BASE_URL ="https://bal.kg/api/";

    public static BalApi getApi() {

        return RetrofitClient.getClient(BASE_URL).create(BalApi.class);
        /* RetrofitHolder.getRetrofit().create(BalAPI.class);*/
    }

}
