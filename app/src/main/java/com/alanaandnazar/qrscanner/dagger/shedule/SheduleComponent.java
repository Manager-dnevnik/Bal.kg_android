package com.alanaandnazar.qrscanner.dagger.shedule;

import com.alanaandnazar.qrscanner.parent.schedule.ScheduleActivity;

import dagger.Subcomponent;

@Subcomponent(modules = SheduleModule.class)
@ScheduleScope
public interface SheduleComponent {
    void inject(ScheduleActivity activity);
}
