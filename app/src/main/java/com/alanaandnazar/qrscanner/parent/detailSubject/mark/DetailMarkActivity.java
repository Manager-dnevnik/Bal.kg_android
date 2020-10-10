package com.alanaandnazar.qrscanner.parent.detailSubject.mark;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Mark;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.widget.Toast.LENGTH_SHORT;

public class DetailMarkActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    MarkAdapter adapter;
    int id, subject_id;
    String date;
    Toolbar toolbar;
    private List<Mark> mMarkList;
    private Mark mMark;

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
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void init() {
        id = getIntent().getIntExtra("id", 0);
        subject_id = getIntent().getIntExtra("subject_id", 0);
        String subject = getIntent().getStringExtra("subject");
        date = getIntent().getStringExtra("subject_date");
        toolbar.setTitle(subject);
        adapter = new MarkAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        token = saveToken.getToken(this);
        Timber.e(token);
        getChildren();
    }

    public void getChildren() {
        BalApi balApi = App.getApi();
        balApi.getSubject(token, id, subject_id).enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(@NonNull Call<List<Mark>> call, @NonNull Response<List<Mark>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Timber.e("%s", response.body().size());
                        for (Mark mark : response.body()
                        ) {
                            if (mark.getDate().equals(date)) {
                                mMark = mark;
                            }
                            Timber.e("MARK_DATE: " + mark.getDate() + " SUBJECT_DATE:" + date);

                        }
                        mMarkList = response.body();
                        showSingleMark(mMark);
                    }
                } else {
                    Toast.makeText(DetailMarkActivity.this, "Пусто", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Mark>> call, @NonNull Throwable t) {
                Toast.makeText(DetailMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mark_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show_all_marks) {
            if (item.getTitle() == getString(R.string.show_all_marks)) {
                showAllMarks(mMarkList);
                item.setTitle(getString(R.string.show_mark_only_for_today));
            } else if (item.getTitle() == getString(R.string.show_mark_only_for_today)) {
                showSingleMark(mMark);
                item.setTitle(getString(R.string.show_all_marks));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAllMarks(List<Mark> markList) {
        if (markList == null) {
            Toast.makeText(this, "Пусто", LENGTH_SHORT).show();
        } else {
            adapter.setItems(markList);
        }
    }

    public void showSingleMark(Mark mark) {
        if (mark != null) {
            List<Mark> temp = new ArrayList<>();
            temp.add(mark);
            adapter.setItems(temp);
        } else {
            Toast.makeText(this, "Сегодня нет оценок", LENGTH_SHORT).show();
        }
    }
}
