package com.alanaandnazar.qrscanner.parent.child_move.view;


import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.IProgressBar;

import java.util.List;

public interface IChildMoveView extends IProgressBar {

    void onSuccess(List<ChildMove> childMove);

    void showError(String message);

}
