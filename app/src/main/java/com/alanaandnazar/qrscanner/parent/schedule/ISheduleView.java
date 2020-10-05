package com.alanaandnazar.qrscanner.parent.schedule;


import com.alanaandnazar.qrscanner.model.Schedule;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.IProgressBar;

import java.util.List;

public interface ISheduleView extends IProgressBar {

    void onSuccess(List<Schedule> shedules);

    void showError(String message);

}
