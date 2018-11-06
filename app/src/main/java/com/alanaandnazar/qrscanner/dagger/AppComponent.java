package com.alanaandnazar.qrscanner.dagger;

import com.alanaandnazar.qrscanner.dagger.child_move.ChildMoveComponent;
import com.alanaandnazar.qrscanner.dagger.child_move.ChildMoveModule;

import javax.inject.Singleton;
import dagger.Component;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    ChildMoveComponent plus(ChildMoveModule loginModule);

}
