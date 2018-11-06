package com.alanaandnazar.qrscanner.parent.child_move.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.BaseApplication;
import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.dagger.child_move.ChildMoveModule;
import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.ChildMoveAdapter;
import com.alanaandnazar.qrscanner.parent.child_move.presenter.IChildMovePresenter;

import java.util.List;

import javax.inject.Inject;

public class ChildMoveActivity extends AppCompatActivity implements IChildMoveView, ChildMoveAdapter.OnOrderListener {

    ProgressDialog progressBar;
    RecyclerView recyclerView;
    ChildMoveAdapter adapter;

    @Inject
    IChildMovePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_move);
        initComponents();
        init();
    }
    private void initComponents() {
        BaseApplication application = (BaseApplication) getApplicationContext();
        application.getAppComponent().plus(new ChildMoveModule()).inject(this);
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ChildMoveAdapter(ChildMoveActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        int id = getIntent().getIntExtra("id", 0);

        presenter.bindView(this);
        presenter.getChildMoves(id);
    }

    @Override
    public void onSuccess(List<ChildMove> childMove) {
        adapter.updateItems(childMove);
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
    public void onOrderClick(ChildMove childMove, int position) {

    }
}
