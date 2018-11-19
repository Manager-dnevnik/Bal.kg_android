package com.alanaandnazar.qrscanner.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.activity.LoginActivity;
import com.alanaandnazar.qrscanner.model.Classe;
import com.alanaandnazar.qrscanner.parent.ParentActivity;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import com.alanaandnazar.qrscanner.teacher.children.ChildrenActivity;
import com.alanaandnazar.qrscanner.teacher.shedule.TeacherSheduleActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherActivity extends AppCompatActivity implements ClasseAdapter.OnOrderListener {

    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    ClasseAdapter adapter;
    Toolbar toolbar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Учитель");
        init();

    }

    private void init() {
        adapter = new ClasseAdapter(TeacherActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(TeacherActivity.this);
        Log.e("TOKEN", token);
        getClasses();
    }

    public void getClasses() {

        BalAPI balAPI = App.getApi();
        balAPI.getClasses(token).enqueue(new Callback<List<Classe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Classe>> call, @NonNull Response<List<Classe>> response) {

                if (response.code() == 401) {

                    saveToken.ClearToken(TeacherActivity.this);

                    Intent i = new Intent(TeacherActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.e("Classes SIZE", response.body().size()+"");
                        adapter.updateItems(response.body());
                    }
                } else {
                    Toast.makeText(TeacherActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Classe>> call, Throwable t) {
                Toast.makeText(TeacherActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOrderClick(Classe classe, int position) {
        Intent intent = new Intent(this, TeacherSheduleActivity.class);
        intent.putExtra("id", classe.getId());
        startActivity(intent);
    }

    public void onClick(View view) {
        saveToken.ClearToken(TeacherActivity.this);
        Intent i = new Intent(TeacherActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
