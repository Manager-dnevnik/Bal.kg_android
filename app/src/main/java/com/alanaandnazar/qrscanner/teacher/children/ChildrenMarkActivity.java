package com.alanaandnazar.qrscanner.teacher.children;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Student;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;
import com.alanaandnazar.qrscanner.teacher.subject.SubjectTeacherActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildrenMarkActivity extends AppCompatActivity implements ChildrenAdapter.OnOrderListener {

    Spinner partSpinner;
    String[] parts = {"Четверть:", "1", "2", "3", "4"};
    String time;
    Switch aSwitch;
    EditText reviews;
    String mark = "0";
    String type_mark = "0";
    String part = "0";
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    ProgressDialog progressBar;
    Toolbar toolbar;

    RecyclerView recyclerView;
    ChildrenAdapter adapter;
    int class_id;
    int subject_id;
    TextView name;
    List<Student> arrayList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_children);

        initToolbar();
        arrayList = new ArrayList<>();
        partSpinner = findViewById(R.id.partSpinner);
        aSwitch = findViewById(R.id.part_swith);
        reviews = findViewById(R.id.reviews);
        name = findViewById(R.id.name);

        token = saveToken.getToken(ChildrenMarkActivity.this);
        class_id = getIntent().getIntExtra("class_id", 0);
        subject_id = getIntent().getIntExtra("subject_id", 0);
        String subject = getIntent().getStringExtra("subject");
        name.setText(subject);
        final ArrayAdapter<String> partsAdapter = new ArrayAdapter<String>(this, R.layout.spiener_item, parts);
        partsAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
        partSpinner.setAdapter(partsAdapter);

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("dd.mm.yyyy").format(new Date());

        Log.e("TIME", timeStamp);

        partSpinner.setOnTouchListener((v, event) -> {
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            return false;
        });


        partSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                part = partSpinner.getSelectedItem().toString();

                Toast.makeText(ChildrenMarkActivity.this, part, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                type_mark = "0";
            } else {
                type_mark = "part";
            }
        });

        init();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Поставить оценку");
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new ChildrenAdapter(ChildrenMarkActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        token = saveToken.getToken(ChildrenMarkActivity.this);
        getStudents();
    }


    public void onClickMark(View view) {
        String review = reviews.getText().toString();
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(new Date());

        boolean bool = true;
        if (review.length() == 0) {
            Toast.makeText(this, "Комментарии ...", Toast.LENGTH_SHORT).show();
            bool = false;
        }


        if (partSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Выберите четверть!", Toast.LENGTH_SHORT).show();
            bool = false;
        }

        if (bool) {
//            createMark(token, idChild, subject_id, mark, timeStamp, type_mark, part, review);
            startMark(token, subject_id, timeStamp, type_mark, part, review);
        }
    }


    public void getStudents() {

        BalApi balAPI = App.getApi();
        balAPI.getStudents(token, class_id).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        arrayList = response.body();
                        adapter.updateItems(arrayList);
                    }
                } else {
                    Toast.makeText(ChildrenMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(ChildrenMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void startMark(String token, int subject_id, String timeStamp, String type_mark, String part, String review) {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Загрузка...");
        progressBar.show();

        new Thread(() -> {
            try {
                postCriteries(token, subject_id, timeStamp, type_mark, part, review);
                runOnUiThread(() -> {
                    progressBar.dismiss();
                    Toast.makeText(ChildrenMarkActivity.this, "Успешно отправлено!", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.dismiss();
                    Toast.makeText(ChildrenMarkActivity.this, "Ошибка отправки!", Toast.LENGTH_SHORT).show();
                });
            }

        }).start();

    }

    private void postCriteries(String token, int subjectId, String date, String type_mark, String part, String comm) throws Exception {
        BalApi api = App.getApi();
        Call<ResponseBody> call;
        for (Student student : arrayList) {

            if (student.getMark_position() != 0) {
                Log.e("MARK", student.getMark() + " " + student.getFio());
                Log.e("MARK", token + " " + student.getId() + " " + subjectId + " " + student.getMark() + " " + date + " " + type_mark + " " + part + " " + comm);
                call = api.createMark(token, student.getId(), subjectId, student.getMark(), date, type_mark, part, comm);
                Response<ResponseBody> response = call.execute();


                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.e("Response : ", response.body().toString() + " - " + response.body().string());
                } else {
                    throw new Exception();
                }
            }
        }
    }

    public void createMark(String token, int id, int subjectId, String mark, String date, String type_mark, String part, String comm) {

        Log.e("CREATE", id + " " + subjectId + " " + mark + " " + date + " " + type_mark + " " + part + " " + comm);

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Отправка ...");
        progressBar.show();

        BalApi balAPI = App.getApi();
        balAPI.createMark(token, id, subjectId, mark, date, type_mark, part, comm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.e("CODE", response.code() + "");
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        progressBar.dismiss();

                        finish();
                        Toast.makeText(ChildrenMarkActivity.this, "Успешно!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (progressBar != null && progressBar.isShowing()) progressBar.dismiss();
                    progressBar = null;
                    try {
                        assert response.errorBody() != null;
                        Log.e("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(ChildrenMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressBar.dismiss();
                Log.e("FAIL", t.getMessage());

                Toast.makeText(ChildrenMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onOrderClick(Student student, int position) {
//        Intent intent = new Intent(this, MarkActivity.class);
//        intent.putExtra("class_id", children.getId());
//        intent.putExtra("name", children.getFio());
//        startActivity(intent);
    }

    public void onClick(View view) {
        Intent i = new Intent(ChildrenMarkActivity.this, SubjectTeacherActivity.class);
        i.putExtra("class_id", class_id);
        startActivity(i);
    }


}
