package com.alanaandnazar.qrscanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;

/**
 * Created by Erlan on 26.09.2017.
 */

public class SplashScreenActivity  extends AppCompatActivity{
SaveUserToken saveUserToken = new SaveUserToken();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView imageView = findViewById(R.id.splash_image);

        Glide.with(SplashScreenActivity.this).load(R.mipmap.end_main_splash_icon).into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!saveUserToken.getToken(SplashScreenActivity.this).equals("empty")) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },1000);



    }
}