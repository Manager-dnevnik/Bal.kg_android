package com.alanaandnazar.qrscanner.dagger.shedule;


import android.content.Context;

import com.alanaandnazar.qrscanner.parent.schedule.presenter.IShedulePresenter;
import com.alanaandnazar.qrscanner.parent.schedule.presenter.ShedulePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SheduleModule {

    @Provides
    IShedulePresenter providePresenter(Context context) {
        return new ShedulePresenter(context);
    }
}
