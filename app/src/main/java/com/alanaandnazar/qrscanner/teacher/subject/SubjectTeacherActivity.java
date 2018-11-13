package com.alanaandnazar.qrscanner.teacher.subject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Subject;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import com.alanaandnazar.qrscanner.teacher.CreateHomeWorkActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectTeacherActivity extends AppCompatActivity implements SubjectAdapter.OnOrderListener {

    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    SubjectAdapter adapter;
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
        toolbar.setTitle("Выберите предмет");
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
        adapter = new SubjectAdapter(SubjectTeacherActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(SubjectTeacherActivity.this);
        Log.e("TOKEN", token);
        getSubject();
    }

    public void getSubject() {

        BalAPI balAPI = App.getApi();
        balAPI.getSubjectsMark(token).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(@NonNull Call<List<Subject>> call, @NonNull Response<List<Subject>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.e("Classes SIZE", response.body().size()+"");
                        adapter.updateItems(response.body());
                    }
                } else {
                    Toast.makeText(SubjectTeacherActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Toast.makeText(SubjectTeacherActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOrderClick(Subject subject, int position) {
        Intent intent = new Intent(this, CreateHomeWorkActivity.class);
        intent.putExtra("subject_id", subject.getId());
        intent.putExtra("class_id", id);
        intent.putExtra("name", subject.getName());
        startActivity(intent);
    }

//    public void onClick(View view) {
//        Intent i = new Intent(SubjectTeacherActivity.this, CreateHomeWorkActivity.class);
//        i.putExtra("id", id);
//        startActivity(i);
//    }
}
