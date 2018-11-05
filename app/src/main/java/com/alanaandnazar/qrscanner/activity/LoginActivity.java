package com.alanaandnazar.qrscanner.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.parent.ParentActivity;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import com.alanaandnazar.qrscanner.retrofit.TokenResponse;
import com.alanaandnazar.qrscanner.teacher.TeacherActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alanaandnazar.qrscanner.activity.MainActivity.isNetworkStatusAvialable;

public class LoginActivity extends AppCompatActivity {
    EditText loginEdit, passwordEdit;
    Button btnEnter;
    SaveUserToken saveUserToken = new SaveUserToken();
    BalAPI balAPI;
    String login, password;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEdit = findViewById(R.id.login);
        passwordEdit = findViewById(R.id.password);
        btnEnter = findViewById(R.id.btnEnter);
      /*  textView = findViewById(R.id.txtAPI);

        textView.setText(Constants.BASE_API);
        retrofit1 = new Retrofit.Builder()
                .baseUrl(Constants.BASE_API)
                // RxJava
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())/
                .addConverterFactory(GsonConverterFactory.create())
                //.client(client)
                .build();
        RetrofitHolder.setRetrofit(retrofit1);
        //Log.e("OnCreate", "onCreate: " + saveUserToken.getToken(LoginActivity.this));

*/
        btnEnter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        btnEnter.setBackgroundResource(R.drawable.mybuttonclicked);
                        btnEnter.setTextColor(Color.parseColor("#fdfdfe"));
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        btnEnter.setBackgroundResource(R.drawable.mybutton);
                        btnEnter.setTextColor(Color.parseColor("#7a1a8b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        btnEnter.setBackgroundResource(R.drawable.mybutton);
                        btnEnter.setTextColor(Color.parseColor("#7a1a8b"));
                        break;
                }

                return false;
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkStatusAvialable(getApplicationContext())) {
                    login = loginEdit.getText().toString();
                    password = convertPassMd5(passwordEdit.getText().toString());
                  /*  if (login.equals("Admin") && passwordEdit.getText().toString().equals("adminbalkg")) {
                        Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Log.e("TAG", "MD5: " + password + " login " + login);

                        authPost(login, password);
                    }*/
                    Log.e("TAG", "MD5: " + password + " login " + login);

                    AuthPost(login, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Проверьте подключение к сети!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void AuthPost(String log, String pass) {

        balAPI = App.getApi();

        RequestBody login = RequestBody.create(MediaType.parse("text/plain"), log);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), pass);

        Log.e("PASS", pass);

        balAPI.authPost(login, password).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                Log.e("TAG", "message " + response.message());
                Log.e("TAG", "message " + response.body());

                if (response.body() != null) {
                    if (response.body().getStatus().equals("ok")) {
                        Toast.makeText(LoginActivity.this, "Авторизация прошла успешно", Toast.LENGTH_SHORT).show();
                        saveUserToken.saveToken(response.body().getToken(), LoginActivity.this);//Save Token

                        Class clazz = null;
                        if (response.body().getUser_type().equals("parent")) {
                            clazz = ParentActivity.class;
                        }else if (response.body().getUser_type().equals("teacher")) {
                            clazz = TeacherActivity.class;
                        }else {
                            clazz = MainActivity.class;
                        }
                        Intent i = new Intent(LoginActivity.this, clazz);
                        startActivity(i);
                        finish();
                    } else {
                        loginEdit.setError("!");
                        passwordEdit.setError("!");
                        Toast.makeText(LoginActivity.this, response.body().getMess(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Сервер не отвечает или неправильный Адрес сервера! ",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
            }
        });
    }

    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}