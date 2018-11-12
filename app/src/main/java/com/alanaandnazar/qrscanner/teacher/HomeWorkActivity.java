package com.alanaandnazar.qrscanner.teacher;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.alanaandnazar.qrscanner.teacher.mark.MarkActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeWorkActivity extends AppCompatActivity {

    Spinner subjectSpinner;
    TextView textDate;
    EditText homework;
    ProgressDialog progressBar;
    SaveUserToken saveToken = new SaveUserToken();
    String token;
    int idSubject;
    Calendar myCalendar = Calendar.getInstance();

    TextView textView;

    @Override
    protected void onResume() {
        Log.e("TAG", "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.e("TAG", "onRestrart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.e("TAG", "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.e("TAG", "onPause");
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);
        Log.e("TAG", "onCreate");

        token = saveToken.getToken(HomeWorkActivity.this);

        int a = 1212;
        String b = "dasdasd";

        textView = findViewById(R.id.textView);
        textView.setTextSize(30);
        textView.setText("Android");

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeWorkActivity.this, "BUTTON", Toast.LENGTH_SHORT).show();
                textView.setText("Button");
            }
        });

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
                new DatePickerDialog(HomeWorkActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        getSubjects();
    }

    private void updateLabel() {
        String myFormat = "dd.MM.YYYY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textDate.setText(sdf.format(myCalendar.getTime()));
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
                        final ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(HomeWorkActivity.this, R.layout.spiener_item, list);
                        subjectAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
                        subjectSpinner.setAdapter(subjectAdapter);

                        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {

                                idSubject = subjectList.get(position).getId();

                                Toast.makeText(HomeWorkActivity.this, "" + idSubject, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(HomeWorkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                progressBar.dismiss();

                Log.e("FAIL", t.getMessage());
                Toast.makeText(HomeWorkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickHomeWork(View view) {

    }
}

