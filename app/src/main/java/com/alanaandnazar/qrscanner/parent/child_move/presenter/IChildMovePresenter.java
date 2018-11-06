package com.alanaandnazar.qrscanner.parent.child_move.presenter;

import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.parent.child_move.view.IChildMoveView;

import java.io.File;

public interface IChildMovePresenter extends Lifecycle<IChildMoveView> {

    void getChildMoves(int id);
}
