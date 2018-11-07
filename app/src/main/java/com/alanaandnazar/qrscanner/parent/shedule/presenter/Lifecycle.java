package com.alanaandnazar.qrscanner.parent.shedule.presenter;

public interface Lifecycle<T> {
    void bindView(T t);
    void unbindView();
}
