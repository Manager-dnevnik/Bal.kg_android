package com.alanaandnazar.qrscanner.parent.detailSubject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.parent.detailSubject.fragment.HomeWorkFragment;
import com.alanaandnazar.qrscanner.parent.detailSubject.fragment.MarkFragment;

public class SubjectActivity extends AppCompatActivity {

    Toolbar toolbar;

    TabLayout tabLayout;

    ViewPager viewPager;

    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedules);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);

        init();
    }

    private void init() {
//        initToolbar();
        initViewPager();
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
        adapter.addFrag(new MarkFragment(), "Домашнее задание");
        adapter.addFrag(new HomeWorkFragment(), "Оценки");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}