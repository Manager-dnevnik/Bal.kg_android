package com.alanaandnazar.qrscanner.parent.schedule.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Schedule;
import com.alanaandnazar.qrscanner.parent.schedule.ISheduleView;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShedulePresenter implements IShedulePresenter {

    private ISheduleView view;
    private Context context;
    private Schedule user;

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
        BalApi balAPI = App.getApi();
        balAPI.getSchedules(token, id).enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(@NonNull Call<List<Schedule>> call, @NonNull Response<List<Schedule>> response) {
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
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                view.showError("Сервер не отвечает или неправильный Адрес сервера!");
                view.hideProgress();
            }
        });
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
