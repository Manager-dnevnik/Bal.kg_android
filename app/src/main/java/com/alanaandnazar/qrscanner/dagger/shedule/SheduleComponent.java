package com.alanaandnazar.qrscanner.dagger.shedule;

import com.alanaandnazar.qrscanner.parent.shedule.SheduleActivity;

import dagger.Subcomponent;

@Subcomponent(modules = SheduleModule.class)
@SheduleScope
public interface SheduleComponent {
    void inject(SheduleActivity activity);
}
