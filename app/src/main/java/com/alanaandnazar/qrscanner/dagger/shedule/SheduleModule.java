package com.alanaandnazar.qrscanner.dagger.shedule;


import android.content.Context;

import com.alanaandnazar.qrscanner.parent.child_move.presenter.ChildMovePresenter;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.IChildMovePresenter;
import com.alanaandnazar.qrscanner.parent.shedule.presenter.IShedulePresenter;
import com.alanaandnazar.qrscanner.parent.shedule.presenter.ShedulePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SheduleModule {

    @Provides
    IShedulePresenter providePresenter(Context context) {
        return new ShedulePresenter(context);
    }
}
