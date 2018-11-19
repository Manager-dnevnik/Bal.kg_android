package com.alanaandnazar.qrscanner.parent.shedule;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.alanaandnazar.qrscanner.BaseApplication;
import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.dagger.child_move.ChildMoveModule;
import com.alanaandnazar.qrscanner.dagger.shedule.SheduleModule;
import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.model.Shedule;
import com.alanaandnazar.qrscanner.parent.DividerItemDecorationTwo;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.ChildMoveAdapter;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.IChildMovePresenter;
import com.alanaandnazar.qrscanner.parent.child_move.view.ChildMoveActivity;
import com.alanaandnazar.qrscanner.parent.child_move.view.IChildMoveView;
import com.alanaandnazar.qrscanner.parent.shedule.presenter.IShedulePresenter;
import com.alanaandnazar.qrscanner.parent.shedule.presenter.SheduleAdapter;

import java.util.List;

import javax.inject.Inject;

public class SheduleActivity extends AppCompatActivity implements ISheduleView, SheduleAdapter.OnOrderListener {

    RecyclerView recyclerView;
    SheduleAdapter adapter;
    ProgressDialog progressBar;


    @Inject
    IShedulePresenter presenter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule);
        initComponents();
        initToolbar();
        init();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Расписание");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initComponents() {
        BaseApplication application = (BaseApplication) getApplicationContext();
        application.getAppComponent().plus(new SheduleModule()).inject(this);
    }

    private void init() {
        int id = getIntent().getIntExtra("id", 0);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new SheduleAdapter(SheduleActivity.this, SheduleActivity.this, id);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecorationTwo(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecorationTwo(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);


        presenter.bindView(this);
        presenter.getShedules(id);
    }


    @Override
    public void onSuccess(List<Shedule> shedules) {
        adapter.updateItems(shedules);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Загрузка...");
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        if (progressBar != null && progressBar.isShowing()) progressBar.dismiss();
        progressBar = null;
    }

    @Override
    public void onOrderClick(Shedule shedule, int position) {

    }
}
