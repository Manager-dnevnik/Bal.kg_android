package com.alanaandnazar.qrscanner.parent.schedule.presenter;

import com.alanaandnazar.qrscanner.parent.schedule.ISheduleView;

public interface IShedulePresenter extends Lifecycle<ISheduleView> {

    void getShedules(int id);
}
