package com.alanaandnazar.qrscanner.parent;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.activity.LoginActivity;
import com.alanaandnazar.qrscanner.model.Student;
import com.alanaandnazar.qrscanner.parent.announcement.AnnouncementActivity;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentActivity extends AppCompatActivity implements StudentsAdapter.onStudentClickListener {

    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    StudentsAdapter adapter;
    Toolbar toolbar;
    ImageView notify;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        recyclerView = findViewById(R.id.recyclerView);
        notify = findViewById(R.id.notify);
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Родитель");
        init();

        notify.setOnClickListener(v -> startActivity(new Intent(ParentActivity.this, AnnouncementActivity.class)));
    }

    private void init() {
        adapter = new StudentsAdapter(ParentActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(ParentActivity.this);
        Log.e("TOKEN", token);
        getChildren();
    }

    public void getChildren() {

        BalApi balAPI = App.getApi();
        balAPI.getMyChildren(token).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                if (response.code() == 401) {

                    saveToken.clearToken(ParentActivity.this);

                    Intent i = new Intent(ParentActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.e("CHILD SIZE", response.body().size() + "");
                        adapter.updateItems(response.body());
                    }
                } else {

                    Toast.makeText(ParentActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Student>> call, @NonNull Throwable t) {
                Toast.makeText(ParentActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStudentClick(Student student, int position) {
        Intent intent = new Intent(this, InfoChildActivity.class);
        intent.putExtra("id", student.getId());
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
            saveToken.clearToken(ParentActivity.this);
            Intent i = new Intent(ParentActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

}
