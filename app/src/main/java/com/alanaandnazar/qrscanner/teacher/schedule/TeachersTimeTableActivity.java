package com.alanaandnazar.qrscanner.teacher.schedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Schedule;
import com.alanaandnazar.qrscanner.parent.DividerItemDecorationTwo;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeachersTimeTableActivity extends AppCompatActivity implements TeachersTimeTableAdapter.OnOrderListener {

    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    TeachersTimeTableAdapter adapter;
    Toolbar toolbar;
    int id;
    Button btn_create;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        recyclerView = findViewById(R.id.recyclerView);
        btn_create = findViewById(R.id.btn_create);
        btn_create.setVisibility(View.GONE);
        toolbar = findViewById(R.id.toolbar);
        initToolbar();
        init();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Расписание");
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }



    private void init() {
        id = getIntent().getIntExtra("id", 0);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TeachersTimeTableAdapter(TeachersTimeTableActivity.this, TeachersTimeTableActivity.this, id);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecorationTwo(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecorationTwo(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(TeachersTimeTableActivity.this);
        Log.e("TOKEN", token);
        getSubject();
    }

    public void getSubject() {

        BalApi balAPI = App.getApi();
        balAPI.getShedulesTeacher(token, id).enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(@NonNull Call<List<Schedule>> call, @NonNull Response<List<Schedule>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.e("Classes SIZE", response.body().size()+"");
                        adapter.updateItems(response.body());
                    }
                } else {
                    Toast.makeText(TeachersTimeTableActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Schedule>> call, Throwable t) {
                Toast.makeText(TeachersTimeTableActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOrderClick(Schedule shedule, int position) {

    }

//    public void onClick(View view) {
//        Intent i = new Intent(SubjectTeacherActivity.this, CreateHomeWorkActivity.class);
//        i.putExtra("id", id);
//        startActivity(i);
//    }
}
