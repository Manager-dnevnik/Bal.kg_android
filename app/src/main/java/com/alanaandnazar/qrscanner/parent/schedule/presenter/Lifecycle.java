package com.alanaandnazar.qrscanner.parent.schedule.presenter;

public interface Lifecycle<T> {
    void bindView(T t);
    void unbindView();
}
