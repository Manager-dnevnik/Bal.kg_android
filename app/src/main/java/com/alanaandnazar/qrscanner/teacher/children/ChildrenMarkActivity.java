package com.alanaandnazar.qrscanner.teacher.children;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Children;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import com.alanaandnazar.qrscanner.teacher.subject.SubjectTeacherActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildrenMarkActivity extends AppCompatActivity implements ChildrenAdapter.OnOrderListener {

    Spinner partSpinner;
    String parts[] = {"Четверть:", "1", "2", "3", "4"};
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
    List<Children> arrayList;

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

        String timeStamp = new SimpleDateFormat("dd.mm.yyyy").format(new Date());

        Log.e("TIME", timeStamp);

        partSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
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

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    type_mark = "0";
                } else {
                    type_mark = "part";
                }
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new ChildrenAdapter(ChildrenMarkActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);

        token = saveToken.getToken(ChildrenMarkActivity.this);
        getChildrens();
    }


    public void onClickMark(View view) {
        String review = reviews.getText().toString();
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


    public void getChildrens() {

        BalAPI balAPI = App.getApi();
        balAPI.getChildrens(token, class_id).enqueue(new Callback<List<Children>>() {
            @Override
            public void onResponse(@NonNull Call<List<Children>> call, @NonNull Response<List<Children>> response) {
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
            public void onFailure(Call<List<Children>> call, Throwable t) {
                Toast.makeText(ChildrenMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void startMark(String token, int subject_id, String timeStamp, String type_mark, String part, String review) {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Загрузка...");
        progressBar.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    postCriterias(token, subject_id, timeStamp, type_mark, part, review);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            Toast.makeText(ChildrenMarkActivity.this, "Успешно отправлено!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.dismiss();
                            Toast.makeText(ChildrenMarkActivity.this, "Ошибка отправки!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).start();

    }

    private void postCriterias(String token, int subjectId, String date, String type_mark, String part, String comm) throws Exception {
        BalAPI api = App.getApi();
        Call<ResponseBody> call;
        for (Children children : arrayList) {
            if (children.getMark_position()!=0){
                Log.e("MARK", children.getMark() + " " + children.getFio());
                Log.e("MARK", token + " " + children.getId() + " " + subjectId + " " + children.getMark() + " " + date + " " + type_mark + " " + part + " " + comm);
                call = api.createMark(token, children.getId(), subjectId, children.getMark(), date, type_mark, part, comm);
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    Log.e("Responce : ", response.body().toString() + " - " + response.body().string());
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

        BalAPI balAPI = App.getApi();
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
                        Log.e("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(ChildrenMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.dismiss();
                Log.e("FAIL", t.getMessage());

                Toast.makeText(ChildrenMarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onOrderClick(Children children, int position) {
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
