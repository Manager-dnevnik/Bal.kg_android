package com.alanaandnazar.qrscanner.parent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Student;
import com.alanaandnazar.qrscanner.parent.child_move.view.ChildSchoolVisitTimeActivity;
import com.alanaandnazar.qrscanner.parent.homework.ParentHomeworkActivity;
import com.alanaandnazar.qrscanner.parent.mark.MarkActivity;
import com.alanaandnazar.qrscanner.parent.announcement.AnnouncementActivity;
import com.alanaandnazar.qrscanner.parent.schedule.ScheduleActivity;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;
import com.bumptech.glide.Glide;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class InfoChildActivity extends AppCompatActivity {

    SaveUserToken saveUserToken = new SaveUserToken();
    String token;
    int id;
    ImageView imageView;
    TextView textName, textParent1, textParent2, textPhone, textClass, textSchool, move_status;
    Button btn_child_move, btnSchedule, btnMark, btnHomework, btnNote;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detai_child);

        initToolbar();
        init();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void init() {
        imageView = findViewById(R.id.imageView);
        textName = findViewById(R.id.textName);
        textParent1 = findViewById(R.id.textParent1);
        textParent2 = findViewById(R.id.textParent2);
        textPhone = findViewById(R.id.textPhone);
        textClass = findViewById(R.id.textClass);
        textSchool = findViewById(R.id.textSchool);
        btn_child_move = findViewById(R.id.btn_child_move);
        btnSchedule = findViewById(R.id.btn_shedule);
        move_status = findViewById(R.id.move_status);
        btnMark = findViewById(R.id.btn_mark);
        btnHomework = findViewById(R.id.btn_homework);
        btnNote = findViewById(R.id.btn_note);

        id = getIntent().getIntExtra("id", 0);
        token = saveUserToken.getToken(InfoChildActivity.this);

        getChildrenInfo();

        btn_child_move.setOnClickListener(v -> {
            Intent intent = new Intent(InfoChildActivity.this, ChildSchoolVisitTimeActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(InfoChildActivity.this, ScheduleActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        btnMark.setOnClickListener(v -> {
            Intent intent = new Intent(InfoChildActivity.this, MarkActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        btnHomework.setOnClickListener(v -> {
            Intent intent = new Intent(InfoChildActivity.this, ParentHomeworkActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        btnNote.setOnClickListener(v -> {
            Intent intent = new Intent(InfoChildActivity.this, AnnouncementActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
    }

    public void getChildrenInfo() {

        BalApi balAPI = App.getApi();
        balAPI.getChildInfo(token, id).enqueue(new Callback<Student>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Student> call, @NonNull Response<Student> response) {
                Timber.e("%s", response.body());

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Student student = response.body();
                        textName.setText(student.getLast_name() + " " + student.getFirst_name() + " " + student.getSecond_name());
                        textParent1.setText(student.getParent_1());
                        textParent2.setText(student.getParent_2());
                        textClass.setText(student.getGrade());
                        textSchool.setText(student.getSchool());
                        textPhone.setText(student.getPhone());

                        toolbar.setTitle(student.getFirst_name());
                        Glide.with(InfoChildActivity.this).load("https://bal.kg/" + student.getImg()).into(imageView);

                        move_status.setText(response.body().getMove_about());

                        if (response.body().getMove_status() == 0) {
                            move_status.setTextColor(getResources().getColor(R.color.black));
                        } else if (response.body().getMove_status() == 1) {
                            move_status.setTextColor(getResources().getColor(R.color.green));
                        } else if (response.body().getMove_status() == 2) {
                            move_status.setTextColor(getResources().getColor(R.color.red));
                        }

                    }
                } else {
                    Toast.makeText(InfoChildActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Student> call, @NonNull Throwable t) {
                Toast.makeText(InfoChildActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
