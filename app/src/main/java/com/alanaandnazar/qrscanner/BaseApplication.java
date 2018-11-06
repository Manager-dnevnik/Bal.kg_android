package com.alanaandnazar.qrscanner;

import android.app.Application;

import com.alanaandnazar.qrscanner.dagger.AppComponent;
import com.alanaandnazar.qrscanner.dagger.AppModule;
import com.alanaandnazar.qrscanner.dagger.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        appComponent = initDagger();
    }

    private AppComponent initDagger() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


}
