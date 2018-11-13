package com.alanaandnazar.qrscanner.parent;

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
import com.alanaandnazar.qrscanner.activity.MainActivity;
import com.alanaandnazar.qrscanner.model.Children;
import com.alanaandnazar.qrscanner.parent.note.NoteActivity;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import com.alanaandnazar.qrscanner.retrofit.TokenResponse;
import com.alanaandnazar.qrscanner.teacher.TeacherActivity;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentActivity extends AppCompatActivity implements ChildrenAdapter.OnOrderListener {

    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    ChildrenAdapter adapter;
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

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentActivity.this, NoteActivity.class));
            }
        });
    }

    private void init() {
        adapter = new ChildrenAdapter(ParentActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(ParentActivity.this);
        Log.e("TOKEN", token);
        getChildrens();
    }

    public void getChildrens() {

        BalAPI balAPI = App.getApi();
        balAPI.getMyChildrens(token).enqueue(new Callback<List<Children>>() {
            @Override
            public void onResponse(@NonNull Call<List<Children>> call, @NonNull Response<List<Children>> response) {
                if (response.code() == 401) {

                    saveToken.ClearToken(ParentActivity.this);

                    Intent i = new Intent(ParentActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.e("CHILD SIZE", response.body().size()+"");
                        adapter.updateItems(response.body());
                    }
                } else {

                    Toast.makeText(ParentActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Children>> call, Throwable t) {
                Toast.makeText(ParentActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOrderClick(Children children, int position) {
        Intent intent = new Intent(this, InfoChildActivity.class);
        intent.putExtra("id", children.getId());
        startActivity(intent);
    }

    public void onClick(View view) {
        saveToken.ClearToken(ParentActivity.this);

        Intent i = new Intent(ParentActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
