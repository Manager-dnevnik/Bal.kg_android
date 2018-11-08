package com.alanaandnazar.qrscanner.teacher.mark;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MarkActivity extends AppCompatActivity {

    Spinner spinner;
    String marks[] = {"Оценка:", "2", "3", "4", "5"};
    String marks_part[] = {"Оценка за четерть:", "2", "3", "4", "5"};
    String time;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        spinner = findViewById(R.id.markSpinner);
        aSwitch = findViewById(R.id.part_swith);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spiener_item, marks);
        dataAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
        spinner.setAdapter(dataAdapter);

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String mark = spinner.getSelectedItem().toString();
                Toast.makeText(MarkActivity.this, mark, Toast.LENGTH_SHORT).show();
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
                } else {
                    final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MarkActivity.this, R.layout.spiener_item, marks_part);
                    dataAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
                    spinner.setAdapter(dataAdapter);
                    spinner.setSelection(0);
                }
            }
        });

    }
}
