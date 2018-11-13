package com.alanaandnazar.qrscanner.parent.child_move.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.parent.child_move.view.IChildMoveView;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildMovePresenter implements IChildMovePresenter {

    private IChildMoveView view;
    private Context context;
    private ChildMove user;

    SaveUserToken saveUserToken = new SaveUserToken();
    String token;

    public ChildMovePresenter(Context context) {
        this.context = context;
        token = saveUserToken.getToken(context);
    }

    @Override
    public void bindView(IChildMoveView iProfileView) {
        this.view = iProfileView;
        token = saveUserToken.getToken(context);
    }

    @Override
    public void unbindView() {

    }

    @Override
    public void getChildMoves(int id) {
        view.showProgress();
        BalAPI balAPI = App.getApi();
        balAPI.getChildMove(token, id).enqueue(new Callback<List<ChildMove>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChildMove>> call, @NonNull Response<List<ChildMove>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        view.onSuccess(response.body());
                        view.hideProgress();
                    }
                } else {
//                    view.showError("Сервер не отвечает или неправильный Адрес сервера!");
                    view.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<List<ChildMove>> call, Throwable t) {
                view.showError("Сервер не отвечает или неправильный Адрес сервера!");
                view.hideProgress();
            }
        });
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
