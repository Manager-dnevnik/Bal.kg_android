package com.alanaandnazar.qrscanner.parent.shedule.presenter;

import com.alanaandnazar.qrscanner.parent.child_move.view.IChildMoveView;
import com.alanaandnazar.qrscanner.parent.shedule.ISheduleView;

public interface IShedulePresenter extends Lifecycle<ISheduleView> {

    void getShedules(int id);
}
