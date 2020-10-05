package com.alanaandnazar.qrscanner.teacher.children;

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
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Student;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;
import com.alanaandnazar.qrscanner.teacher.mark.MarkActivity;
import com.alanaandnazar.qrscanner.teacher.subject.SubjectTeacherActivity;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildrenActivity extends AppCompatActivity implements ChildrenAdapter.OnOrderListener {

    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    ChildrenAdapter adapter;
    Toolbar toolbar;
    int id;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        initToolbar();
        init();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ученики");
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }



    private void init() {
        id = getIntent().getIntExtra("class_id", 0);
        adapter = new ChildrenAdapter(ChildrenActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(ChildrenActivity.this);
        Log.e("TOKEN", token);
        getChildrens();
    }

    public void getChildrens() {

        BalApi balAPI = App.getApi();
        balAPI.getStudents(token, id).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.e("Classes SIZE", response.body().size()+"");
                        adapter.updateItems(response.body());
                    }
                } else {
                    Toast.makeText(ChildrenActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(ChildrenActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOrderClick(Student student, int position) {

        Intent intent = new Intent(this, MarkActivity.class);
        intent.putExtra("class_id", student.getId());
        intent.putExtra("name", student.getFio());
        startActivity(intent);
    }

    public void onClick(View view) {
        Intent i = new Intent(ChildrenActivity.this, SubjectTeacherActivity.class);
        i.putExtra("class_id", id);
        startActivity(i);
    }
}
