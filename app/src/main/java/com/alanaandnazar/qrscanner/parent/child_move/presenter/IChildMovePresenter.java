package com.alanaandnazar.qrscanner.parent.child_move.presenter;

import com.alanaandnazar.qrscanner.parent.child_move.view.IChildMoveView;

public interface IChildMovePresenter extends Lifecycle<IChildMoveView> {

    void getChildMoves(int id);
}
