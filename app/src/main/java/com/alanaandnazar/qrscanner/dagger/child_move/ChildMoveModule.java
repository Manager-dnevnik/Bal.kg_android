package com.alanaandnazar.qrscanner.dagger.child_move;


import android.content.Context;

import com.alanaandnazar.qrscanner.parent.child_move.presenter.ChildMovePresenter;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.IChildMovePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChildMoveModule {

    @Provides
    IChildMovePresenter providePresenter(Context context) {
        return new ChildMovePresenter(context);
    }
}
