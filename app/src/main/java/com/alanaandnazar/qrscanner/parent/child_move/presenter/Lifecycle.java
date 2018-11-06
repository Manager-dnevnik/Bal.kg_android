package com.alanaandnazar.qrscanner.parent.child_move.presenter;

public interface Lifecycle<T> {
    void bindView(T t);
    void unbindView();
}
