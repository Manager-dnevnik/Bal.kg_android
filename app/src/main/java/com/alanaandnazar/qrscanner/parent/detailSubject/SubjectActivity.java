package com.alanaandnazar.qrscanner.parent.detailSubject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.parent.detailSubject.home_work.HomeWorkFragment;
import com.alanaandnazar.qrscanner.parent.detailSubject.mark.MarkFragment;

public class SubjectActivity extends AppCompatActivity {

    Toolbar toolbar;

    TabLayout tabLayout;

    ViewPager viewPager;

    private ViewPagerAdapter adapter;
    int id = 0;
    int subject_id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedules);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);

        init();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void init() {
        id = getIntent().getIntExtra("id", 0);
        String subject = getIntent().getStringExtra("subject");
        subject_id = getIntent().getIntExtra("subject_id", 0);
        initToolbar();
        initViewPager();
        toolbar.setTitle(subject);
    }

//    private void initToolbar() {
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(view -> onBackPressed());
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) startActivity(SubjectActivity.class);
        finish();
    }

    private void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        MarkFragment markFragment = new MarkFragment();
        HomeWorkFragment homeWorkFragment = new HomeWorkFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putInt("subject_id", subject_id);
        markFragment.setArguments(args);
        homeWorkFragment.setArguments(args);
        adapter.addFrag(homeWorkFragment, "Домашнее задание");
        adapter.addFrag(markFragment, "Оценки");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}