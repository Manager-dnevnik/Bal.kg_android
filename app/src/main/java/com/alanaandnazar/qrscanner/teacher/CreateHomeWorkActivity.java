package com.alanaandnazar.qrscanner.teacher;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Subject;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateHomeWorkActivity extends AppCompatActivity {

    Spinner subjectSpinner;
    TextView textDate;
    EditText homework;
    ProgressDialog progressBar;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    int idSubject;
    Calendar myCalendar = Calendar.getInstance();

    TextView textView;
    int classID;
    String data;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);
        token = saveToken.getToken(CreateHomeWorkActivity.this);

        classID = getIntent().getIntExtra("class_id", 0);
        idSubject = getIntent().getIntExtra("subject_id", 0);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        textDate = findViewById(R.id.textDate);
        homework = findViewById(R.id.homework);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                myCalendar.setMinimumDate(CalendarDay.from(1900, 1, 1))
                updateLabel();
            }

        };


        textDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateHomeWorkActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        initToolbar();
//        getSubjects();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        String name = getIntent().getStringExtra("name");
        toolbar.setTitle("Домашнее задание ("+name+")");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void updateLabel() {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        textDate.setText(sdf.format(myCalendar.getTime()));
        data = sdf.format(myCalendar.getTime());
    }

    public void getSubjects() {

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Загрузка ...");
        progressBar.show();

        BalAPI balAPI = App.getApi();
        balAPI.getSubjectsMark(token).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(@NonNull Call<List<Subject>> call, @NonNull Response<List<Subject>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        List<Subject> subjectList = new ArrayList<>();
                        Subject subject = new Subject();
                        subject.setId(0);
                        subject.setName("Выберите предмет:");
                        subjectList.add(subject);
                        subjectList.addAll(response.body());

                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < subjectList.size(); i++) {
                            list.add(subjectList.get(i).getName());
                        }
                        final ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(CreateHomeWorkActivity.this, R.layout.spiener_item, list);
                        subjectAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
                        subjectSpinner.setAdapter(subjectAdapter);

                        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {

                                idSubject = subjectList.get(position).getId();

                                Toast.makeText(CreateHomeWorkActivity.this, "" + idSubject, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                        progressBar.dismiss();
                    }
                } else {
                    progressBar.dismiss();
                    try {
                        Log.e("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(CreateHomeWorkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                progressBar.dismiss();

                Log.e("FAIL", t.getMessage());
                Toast.makeText(CreateHomeWorkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickHomeWork(View view) {

        String home = homework.getText().toString();

        boolean bool = true;
        if (home.length() == 0) {
            Toast.makeText(this, "Пусто!", Toast.LENGTH_SHORT).show();
            bool = false;
        }

        if (data == null) {
            Toast.makeText(this, "Выберите дату!", Toast.LENGTH_SHORT).show();
            bool = false;
        }
//        if (subjectSpinner.getSelectedItemPosition() == 0) {
//            Toast.makeText(this, "Выберите предмет!", Toast.LENGTH_SHORT).show();
//            bool = false;
//        }
        if (bool) {
            createHomework(token, classID, idSubject, data, home);
        }
    }


    public void createHomework(String token, int id, int subjectId, String date, String comm) {

        Log.e("CREATE", id + " " + subjectId + " " + date + " " + comm);

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Отправка ...");
        progressBar.show();

        BalAPI balAPI = App.getApi();
        balAPI.createHomework(token, id, subjectId, date, comm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.e("CODE", response.code() + "");
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        progressBar.dismiss();

                        finish();
                        Toast.makeText(CreateHomeWorkActivity.this, "Успешно!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (progressBar != null && progressBar.isShowing()) progressBar.dismiss();
                    progressBar = null;
                    try {
                        Log.e("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(CreateHomeWorkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.dismiss();
                Log.e("FAIL", t.getMessage());

                Toast.makeText(CreateHomeWorkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

