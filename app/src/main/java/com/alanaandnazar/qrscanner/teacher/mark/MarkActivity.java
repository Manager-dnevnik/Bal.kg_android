package com.alanaandnazar.qrscanner.teacher.mark;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.alanaandnazar.qrscanner.model.Mark;
import com.alanaandnazar.qrscanner.model.Subject;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import com.alanaandnazar.qrscanner.teacher.children.ChildrenActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkActivity extends AppCompatActivity {

    Spinner spinner;
    Spinner partSpinner, subjectSpinner;
    String marks[] = {"Выьерите оценку:", "2", "3", "4", "5"};
    String parts[] = {"Выберите четверть:", "1", "2", "3", "4"};
    String marks_part[] = {"Выберите оценку за четерть:", "2", "3", "4", "5"};
    String time;
    Switch aSwitch;
    TextView name;
    EditText reviews;
    String mark = "0";
    String type_mark = "0";
    String part = "0";
    SaveUserToken saveToken = new SaveUserToken();

    int idChild, idSubject;
    String token;
    ProgressDialog progressBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        initToolbar();
        spinner = findViewById(R.id.markSpinner);
        partSpinner = findViewById(R.id.partSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        aSwitch = findViewById(R.id.part_swith);
        name = findViewById(R.id.name);
        reviews = findViewById(R.id.reviews);


        partSpinner.setVisibility(View.GONE);

        token = saveToken.getToken(MarkActivity.this);
        idChild = getIntent().getIntExtra("id", 0);
        String firstName = getIntent().getStringExtra("name");

        Log.e("INTENT", idChild+" "+firstName);

        name.setText(firstName);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spiener_item, marks);
        dataAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
        spinner.setAdapter(dataAdapter);

        final ArrayAdapter<String> partsAdapter = new ArrayAdapter<String>(this, R.layout.spiener_item, parts);
        partsAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
        partSpinner.setAdapter(partsAdapter);

        String timeStamp = new SimpleDateFormat("dd.mm.YYYY").format(new Date());

        Log.e("TIME", timeStamp);

        spinner.setOnTouchListener(new View.OnTouchListener() {
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                mark = spinner.getSelectedItem().toString();
                Toast.makeText(MarkActivity.this, mark, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        partSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                part = partSpinner.getSelectedItem().toString();

                Toast.makeText(MarkActivity.this, part, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MarkActivity.this, R.layout.spiener_item, marks);
                    dataAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
                    spinner.setAdapter(dataAdapter);
                    spinner.setSelection(0);
                    partSpinner.setVisibility(View.GONE);
                    type_mark = "0";
                } else {
                    final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MarkActivity.this, R.layout.spiener_item, marks_part);
                    dataAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
                    spinner.setAdapter(dataAdapter);
                    spinner.setSelection(0);
                    partSpinner.setVisibility(View.VISIBLE);
                    type_mark = "part";
                }
            }
        });

        getSubjects();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Поставить оценку");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public void onClick(View view) {
        String review = reviews.getText().toString();
        String timeStamp = new SimpleDateFormat("dd.MM.YYYY").format(new Date());

        boolean bool = true;
        if (review.length() == 0){
            Toast.makeText(this, "Комментарии ...", Toast.LENGTH_SHORT).show();
            bool = false;
        }

        if (subjectSpinner.getSelectedItemPosition()==0){
            Toast.makeText(this, "Выберите предмет!", Toast.LENGTH_SHORT).show();
            bool = false;
        }

        if (!aSwitch.isChecked()){
            if (spinner.getSelectedItemPosition()==0){
                Toast.makeText(this, "Поставьте оценку!", Toast.LENGTH_SHORT).show();
                bool = false;
            }
        }else {
            if (partSpinner.getSelectedItemPosition()==0){
                Toast.makeText(this, "Выберите четверть!", Toast.LENGTH_SHORT).show();
                bool = false;
            }
            if (spinner.getSelectedItemPosition()==0){
                Toast.makeText(this, "Поставьте оценку!", Toast.LENGTH_SHORT).show();
                bool = false;
            }
        }

        if (bool) {
            createMark(token, idChild, idSubject, mark, timeStamp, type_mark, part, review);
        }
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
                        for (int i = 0; i < subjectList.size(); i++){
                            list.add(subjectList.get(i).getName());
                        }
                        final ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(MarkActivity.this, R.layout.spiener_item, list);
                        subjectAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
                        subjectSpinner.setAdapter(subjectAdapter);

                        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {

                                idSubject = subjectList.get(position).getId();

                                Toast.makeText(MarkActivity.this, ""+idSubject, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                progressBar.dismiss();

                Log.e("FAIL", t.getMessage());
                Toast.makeText(MarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void createMark(String token, int id, int subjectId, String mark, String date, String type_mark, String part, String comm) {

        Log.e("CREATE", id+ " "+ subjectId+" "+mark+" "+date+" "+type_mark+" "+ part +" " + comm );

        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Отправка ...");
        progressBar.show();

        BalAPI balAPI = App.getApi();
        balAPI.createMark(token,id, subjectId, mark, date, type_mark, part, comm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.e("CODE", response.code()+"");
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        progressBar.dismiss();

                        finish();
                        Toast.makeText(MarkActivity.this, "Успешно!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (progressBar != null && progressBar.isShowing()) progressBar.dismiss();
                    progressBar = null;
                    try {
                        Log.e("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.dismiss();
                Log.e("FAIL", t.getMessage());

                Toast.makeText(MarkActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
