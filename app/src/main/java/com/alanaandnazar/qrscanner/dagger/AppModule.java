package com.alanaandnazar.qrscanner.dagger;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }


    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    Resources provideResources(Context context) {
        return context.getResources();
    }
}
