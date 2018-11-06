package com.alanaandnazar.qrscanner.parent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Children;
import com.alanaandnazar.qrscanner.parent.child_move.view.ChildMoveActivity;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoChildActivity extends AppCompatActivity {

    SaveUserToken saveUserToken = new SaveUserToken();
    String token;
    int id;
    ImageView imageView;
    TextView textName, textParent1, textParent2, textPhone, textClass, textSchool;
    Button btn_child_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detai_child);

        init();
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

        id = getIntent().getIntExtra("id",0);
        token = saveUserToken.getToken(InfoChildActivity.this);

        getChildrenInfo();

        btn_child_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoChildActivity.this, ChildMoveActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    public void getChildrenInfo() {

        BalAPI balAPI = App.getApi();
        balAPI.getChildInfo(token, id).enqueue(new Callback<Children>() {
            @Override
            public void onResponse(@NonNull Call<Children> call, @NonNull Response<Children> response) {
                Log.e("CHILD INFO", response.body()+"");

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Children children = response.body();
                        textName.setText(children.getLast_name()+" "+children.getFirst_name()+" "+children.getSecond_name());
                        textParent1.setText(children.getParent_1());
                        textParent2.setText(children.getParent_2());
                        textClass.setText(children.getKlass());
                        textSchool.setText(children.getSchool());
                        textPhone.setText(children.getPhone());

                        Glide.with(InfoChildActivity.this).load("https://bal.kg/"+children.getImg()).into(imageView);
                    }
                } else {
                    Toast.makeText(InfoChildActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Children> call, Throwable t) {
                Toast.makeText(InfoChildActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
