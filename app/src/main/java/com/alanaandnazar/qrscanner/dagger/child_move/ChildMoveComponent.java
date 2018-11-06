package com.alanaandnazar.qrscanner.dagger.child_move;

import com.alanaandnazar.qrscanner.parent.child_move.view.ChildMoveActivity;
import dagger.Subcomponent;

@Subcomponent(modules = ChildMoveModule.class)
@ChildMoveScope
public interface ChildMoveComponent {
    void inject(ChildMoveActivity activity);
}
