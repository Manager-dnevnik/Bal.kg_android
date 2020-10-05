package com.alanaandnazar.qrscanner.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.activity.LoginActivity;
import com.alanaandnazar.qrscanner.model.Class;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;
import com.alanaandnazar.qrscanner.teacher.schedule.TeachersTimeTableActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherMainActivity extends AppCompatActivity implements ClassAdapter.OnOrderListener {

    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    ClassAdapter adapter;
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
        adapter = new ClassAdapter(TeacherMainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(TeacherMainActivity.this);
        Log.e("TOKEN", token);
        getClasses();
    }

    public void getClasses() {

        BalApi balAPI = App.getApi();
        balAPI.getClasses(token).enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(@NonNull Call<List<Class>> call, @NonNull Response<List<Class>> response) {

                if (response.code() == 401) {

                    saveToken.clearToken(TeacherMainActivity.this);

                    Intent i = new Intent(TeacherMainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.e("Classes SIZE", response.body().size() + "");
                        adapter.updateItems(response.body());
                    }
                } else {
                    Toast.makeText(TeacherMainActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                Toast.makeText(TeacherMainActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOrderClick(Class classe, int position) {
        Intent intent = new Intent(this, TeachersTimeTableActivity.class);
        intent.putExtra("id", classe.getId());
        startActivity(intent);
    }

    public void onClick(View view) {
        showSignOutDialog();
    }

    private void showSignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выйти");
        builder.setMessage("Вы уверены что хотите выйти?");
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            saveToken.clearToken(TeacherMainActivity.this);
            Intent i = new Intent(TeacherMainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

}
