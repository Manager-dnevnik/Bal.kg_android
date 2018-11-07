package com.alanaandnazar.qrscanner.parent.shedule;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.BaseApplication;
import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.dagger.child_move.ChildMoveModule;
import com.alanaandnazar.qrscanner.dagger.shedule.SheduleModule;
import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.model.Shedule;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule);
        initComponents();
        init();
    }

    private void initComponents() {
        BaseApplication application = (BaseApplication) getApplicationContext();
        application.getAppComponent().plus(new SheduleModule()).inject(this);
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new SheduleAdapter(SheduleActivity.this, SheduleActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        int id = getIntent().getIntExtra("id", 0);

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
