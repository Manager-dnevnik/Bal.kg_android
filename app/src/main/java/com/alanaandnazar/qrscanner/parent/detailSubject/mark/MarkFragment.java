package com.alanaandnazar.qrscanner.parent.detailSubject.mark;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Mark;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MarkFragment extends Fragment {


    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    MarkAdapter adapter;
    int id, subject_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_mark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);

        init();
    }

    private void init() {
        if (getArguments() != null) {
            id = getArguments().getInt("id", 0);
        }
        subject_id = getArguments().getInt("subject_id", 0);

        Timber.tag("MARK_ID").e(id + " " + subject_id);
        adapter = new MarkAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(getActivity());
        Timber.e(token);
        getChildrens();
    }

    public void getChildrens() {

        BalApi balAPI = App.getApi();
        balAPI.getSubject(token, id, subject_id).enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(@NonNull Call<List<Mark>> call, @NonNull Response<List<Mark>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Timber.e("%s", response.body().size());
                        adapter.setItems(response.body());
                    }
                } else {
//                    Toast.makeText(getActivity(), "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Mark>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
