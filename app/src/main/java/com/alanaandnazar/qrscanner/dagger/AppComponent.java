package com.alanaandnazar.qrscanner.dagger;

import com.alanaandnazar.qrscanner.dagger.child_move.ChildMoveComponent;
import com.alanaandnazar.qrscanner.dagger.child_move.ChildMoveModule;
import com.alanaandnazar.qrscanner.dagger.shedule.SheduleComponent;
import com.alanaandnazar.qrscanner.dagger.shedule.SheduleModule;

import javax.inject.Singleton;
import dagger.Component;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    ChildMoveComponent plus(ChildMoveModule childMoveModule);
    SheduleComponent plus(SheduleModule sheduleModule);

}
