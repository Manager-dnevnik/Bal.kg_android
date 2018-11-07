package com.alanaandnazar.qrscanner.parent.shedule.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Shedule;
import com.alanaandnazar.qrscanner.parent.child_move.view.IChildMoveView;
import com.alanaandnazar.qrscanner.parent.shedule.ISheduleView;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShedulePresenter implements IShedulePresenter {

    private ISheduleView view;
    private Context context;
    private Shedule user;

    SaveUserToken saveUserToken = new SaveUserToken();
    String token;

    public ShedulePresenter(Context context) {
        this.context = context;
        token = saveUserToken.getToken(context);
    }

    @Override
    public void bindView(ISheduleView iSheduleView) {
        this.view = iSheduleView;
        token = saveUserToken.getToken(context);
    }

    @Override
    public void unbindView() {

    }

    @Override
    public void getShedules(int id) {
        view.showProgress();
        BalAPI balAPI = App.getApi();
        balAPI.getShedules(token, id).enqueue(new Callback<List<Shedule>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shedule>> call, @NonNull Response<List<Shedule>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        view.onSuccess(response.body());
                        view.hideProgress();
                    }
                } else {
                    view.showError("Сервер не отвечает или неправильный Адрес сервера!");
                    view.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<List<Shedule>> call, Throwable t) {
                view.showError("Сервер не отвечает или неправильный Адрес сервера!");
                view.hideProgress();
            }
        });
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
