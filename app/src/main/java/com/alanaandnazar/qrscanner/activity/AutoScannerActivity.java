package com.alanaandnazar.qrscanner.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.Constants;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Person;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalApi;
import com.alanaandnazar.qrscanner.retrofit.TokenResponse;
import com.google.zxing.Result;

import java.io.BufferedReader;
import java.util.List;

import io.realm.Realm;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static com.alanaandnazar.qrscanner.Constants.STATUS;
import static com.alanaandnazar.qrscanner.activity.MainActivity.isNetworkStatusAvialable;

public class AutoScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private String ID, type, response, token, time;
    private BufferedReader br;
    SaveUserToken saveUserToken = new SaveUserToken();
    Realm realm;
    ProgressDialog progressBar;
    Person person = new Person();
    BalApi balAPI;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        realm = Realm.getDefaultInstance();
        int currentApiVersion = Build.VERSION.SDK_INT;

        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        scannerView.stopCamera();
        if (toast != null) {
            toast.cancel();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0) {

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    (dialog, which) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA},
                                                    REQUEST_CAMERA);
                                        }
                                    }
                            );
                        }
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(AutoScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }

        Log.d("QRCodeAutoScanner", result.getText());
        Log.d("QRCodeAutoScanner", result.getBarcodeFormat().toString());
        response = result.getText();
        int indexOf = response.indexOf(";");

        if (indexOf != -1) {
            ID = response.substring(0, indexOf);
            type = response.substring(indexOf + 1);

        }

        Log.d("ID", ID + "");
        Log.d("type", type + "");
        Log.d("QRCodeAutoScanner", response);

        if (type != null && ID != null) {
            Constants.GET_INFO = true;
            Constants.TYPE = type;
            Constants.ID = ID;

            if (type.equals("guest")) {
                finish();
            } else {
                token = saveUserToken.getToken(AutoScannerActivity.this);
                time = String.valueOf(System.currentTimeMillis() / 1000);

                if (isNetworkStatusAvialable(getApplicationContext())) {
                    Log.e("Время", "onSend: " + time);
                    if (!realm.isEmpty()) {
                        Constants.SYNC = true;

                        final List<Person> data = realm.where(Person.class).findAll();
                        final int size = data.size();

                        Log.e("SIZE", "size: " + size);
                        progressBar = new ProgressDialog(AutoScannerActivity.this);
                        progressBar.setCancelable(false);
                        progressBar.setTitle("Синхронизация... в Базе - " + size);
                        progressBar.show();

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                int i = 0;
                                while (size > i) {
                                    synchronized (this) {
                                        final int finalI = i;

                                        runOnUiThread(() -> {
                                            Log.e("BAZA", "run: " + data.get(finalI));
                                            person = data.get(finalI);
                                            InfoPost(person.getId(), person.getStatus(), person.getType(), person.getTime(), token);
                                            progressBar.setMessage("Отправлено - " + (finalI + 1));
                                        });

                                        try {
                                            wait(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        i++;
                                    }
                                }

                                runOnUiThread(() -> {
                                    if (progressBar.isShowing()) {
                                        realm.beginTransaction();
                                        realm.deleteAll();
                                        realm.commitTransaction();
                                        Toast.makeText(AutoScannerActivity.this, "Успешная синхронизация", Toast.LENGTH_SHORT).show();
                                        Constants.SYNC = false;
                                        progressBar.dismiss();
                                        AutoScannerActivity.this.recreate();
                                    }
                                });
                            }
                        };
                        Thread thread = new Thread(runnable);
                        thread.start();

                    } else {
                        InfoPost(ID, STATUS, type, time, token);
                        if(!Constants.IS_CHECKED){ AutoScannerActivity.this.recreate(); }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AutoScannerActivity.this);
                    builder.setTitle("Оффлайн")
                            .setMessage("Отсутствует подключение к сети, Запись будет добавлена в Базу данных")
                            .setCancelable(false)
                            .setPositiveButton("Ок", (dialog, which) -> {

                                realm.executeTransaction(bgRealm -> {
                                    Person person = bgRealm.createObject(Person.class);
                                    person.setId(ID);
                                    person.setStatus(STATUS);
                                    person.setType(type);
                                    person.setTime(time);
                                });
                                Log.e("DATA_BASE", "onSuccess: " + "Успешно Добавлено в Базу");
                                dialog.cancel();
                                AutoScannerActivity.this.recreate();
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        } else {
            Toast.makeText(AutoScannerActivity.this, "Неправильный QR_CODE!", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    public void InfoPost(String idd, String statuss, String typee, String timee, String tokenn) {

        balAPI = App.getApi();

        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), idd);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), typee);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), statuss);
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), timee);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), tokenn);


        balAPI.infoPost(id, status, type, time, token, null).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {

                //scannerView.resumeCameraPreview(ScannerActivity.this);
                Log.e("TAG", "message " + response.message());
                Log.e("TAG", "message " + response.body());
                if (response.code() == 401) {
                    saveUserToken.clearToken(AutoScannerActivity.this);
                    Toast.makeText(AutoScannerActivity.this, "Сессия устарела, Пожалуйста перезайдите", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(AutoScannerActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                } else {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("ok")) {
                            if (!Constants.SYNC) {
                                if (Constants.IS_CHECKED) {
                                    toast = Toast.makeText(AutoScannerActivity.this, "Успешно \n" + response.body().getFio() + "\n" + response.body().getAbout(), Toast.LENGTH_SHORT);
                                    toast.show();

                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            synchronized (this) {
                                                try {
                                                    wait(500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            runOnUiThread(AutoScannerActivity.this::recreate);
                                        }
                                    };
                                    Thread thread = new Thread(runnable);
                                    thread.start();
                                }
                            }
                        } else {
                            if (progressBar.isShowing()) {
                                progressBar.dismiss();
                            }

                            Toast.makeText(AutoScannerActivity.this, response.body().getMess(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(AutoScannerActivity.this, "Ошибка сервера,данные не отправлены! код= " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t);

                Toast.makeText(AutoScannerActivity.this, "Проверьте подключение к интернету!\n Данные не отправлены!", Toast.LENGTH_LONG).show();
                scannerView.resumeCameraPreview(AutoScannerActivity.this);
            }
        });
    }
}
