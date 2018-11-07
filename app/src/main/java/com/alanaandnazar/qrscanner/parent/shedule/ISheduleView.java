package com.alanaandnazar.qrscanner.parent.shedule;


import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.model.Shedule;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.IProgressBar;

import java.util.List;

public interface ISheduleView extends IProgressBar {

    void onSuccess(List<Shedule> shedules);

    void showError(String message);

}
