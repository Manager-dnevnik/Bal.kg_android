package com.alanaandnazar.qrscanner.parent.detailSubject.mark;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Mark;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMarkActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    MarkAdapter adapter;
    int id, subject_id;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_list);
        recyclerView = findViewById(R.id.recyclerView);
        initToolbar();
        init();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void init() {
        id = getIntent().getIntExtra("id", 0);
        subject_id = getIntent().getIntExtra("subject_id", 0);
        String subject = getIntent().getStringExtra("subject");
        toolbar.setTitle(subject);
        adapter = new MarkAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(this);
        Log.e("TOKEN", token);
        getChildrens();
    }

    public void getChildrens() {

        BalAPI balAPI = App.getApi();
        balAPI.getSubject(token, id, subject_id).enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(@NonNull Call<List<Mark>> call, @NonNull Response<List<Mark>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.e("CHILD SIZE", response.body().size()+"");
                        adapter.updateItems(response.body());
                    }
                } else {
//                    Toast.makeText(getActivity(), "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mark>> call, Throwable t) {
                Toast.makeText(DetailMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
