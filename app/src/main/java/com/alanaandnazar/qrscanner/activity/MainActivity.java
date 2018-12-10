package com.alanaandnazar.qrscanner.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.BuildConfig;
import com.alanaandnazar.qrscanner.Constants;
import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.Token.SaveUserToken;
import com.alanaandnazar.qrscanner.model.Person;
import com.alanaandnazar.qrscanner.retrofit.App;
import com.alanaandnazar.qrscanner.retrofit.BalAPI;
import com.alanaandnazar.qrscanner.retrofit.TokenResponse;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    Intent intent;
    Button btnScanner, btnSendData, exit;
    Spinner spinner;
    Switch aSwitch;
    String categor[] = {"Статус:", "Ученик", "Учитель", "Гость"};
    String categorEn[] = {"Статус:", "children", "teacher", "guest"};
    String ID, type, token, status, time;
    int typeInt;
    EditText editId;
    boolean info;
    Toast toast;
    ImageView guestPhoto;
    CheckBox checkBox;
    private static final int CAMERA_REQUEST = 1888;
    BalAPI balAPI;
    SaveUserToken saveUserToken = new SaveUserToken();
    File file3;
    Uri uri;
    MultipartBody.Part fileToUpload = null;
    TextView textViewEnter, textViewExit, textViewFio, textViewAbout, textViewAPI;
    ProgressDialog progressBar, progressExit;
    Realm realm;
    Person person = new Person();

    static Retrofit retrofit = null;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 12345;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();

        spinner = findViewById(R.id.typeSpinner);
        btnScanner = findViewById(R.id.scanner);
        btnSendData = findViewById(R.id.sendData);
        editId = findViewById(R.id.ID);
        aSwitch = findViewById(R.id.enter);
        guestPhoto = findViewById(R.id.GuestImage);
        exit = findViewById(R.id.exit);
        textViewEnter = findViewById(R.id.txtEnter);
        textViewExit = findViewById(R.id.txtExit);
        textViewFio = findViewById(R.id.fio);
        checkBox = findViewById(R.id.checkBox);
        textViewAbout = findViewById(R.id.about);
        /*textViewAPI = findViewById(R.id.API);
        textViewAPI.setText(Constants.BASE_API);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_API)
                // RxJava
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())/
                .addConverterFactory(GsonConverterFactory.create())
                //.client(client)
                .build();
        RetrofitHolder.setRetrofit(retrofit);*/
        checkPermissionsState();
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spiener_item, categor);
        dataAdapter.setDropDownViewResource(R.layout.spiener_dropdown);
        spinner.setAdapter(dataAdapter);

        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "onResume: ");
        if (Constants.GET_INFO) {
            getInfo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void getInfo() {
        ID = Constants.ID;
        type = Constants.TYPE;

        for (int i = 0; i < categorEn.length; i++) {
            if (categorEn[i].equals(type)) {
                typeInt = i;
            }
        }
        editId.setText(ID);
        spinner.setSelection(typeInt);
        Constants.GET_INFO = false;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setListeners() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    textViewExit.setTextColor(Color.parseColor("#8b8b8b"));
                    textViewExit.setTypeface(Typeface.SANS_SERIF);

                    textViewEnter.setTextColor(Color.parseColor("#692f9a"));
                    textViewEnter.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    textViewEnter.setTextColor(Color.parseColor("#8b8b8b"));
                    textViewEnter.setTypeface(Typeface.SANS_SERIF);

                    textViewExit.setTextColor(Color.parseColor("#692f9a"));
                    textViewExit.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkStatusAvialable(getApplicationContext())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Выход")
                            .setMessage("Вы уверены что хотите выйти?")
                            //.setIcon(R.drawable.ic_android_cat)
                            .setCancelable(false)
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    token = saveUserToken.getToken(MainActivity.this);
                                    LogoutPost(token); // Выход
                                    progressExit = ProgressDialog.show(MainActivity.this, "Пожалуйста подождите", "Выход...");
                                }
                            })
                            .setNegativeButton("Нет",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Проверьте подключение к сети!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        exit.setTextColor(Color.parseColor("#fdfdfe"));
                        exit.setBackgroundResource(R.drawable.mybuttonclicked);
                        // Log.e("DATABASEE", "onTouch: "+realm.isEmpty() );
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        exit.setTextColor(Color.parseColor("#7a1a8b"));
                        exit.setBackgroundResource(R.drawable.mybutton);
                        break;
                    case MotionEvent.ACTION_UP:
                        exit.setTextColor(Color.parseColor("#7a1a8b"));
                        exit.setBackgroundResource(R.drawable.mybutton);
                        break;
                }
                return false;
            }
        });
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
                if (position == 0) {
                    type = "";
                } else {
                    type = categorEn[position];
                }

                if (position == 3) {
                    guestPhoto.setVisibility(View.VISIBLE);
                    guestPhoto.setClickable(true);
                } else {
                    guestPhoto.setVisibility(View.INVISIBLE);
                    guestPhoto.setClickable(false);
                }
                Log.e("CATE", categor[position] + " - ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(i);

            }
        });
        btnScanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        btnScanner.setTextColor(Color.parseColor("#fdfdfe"));
                        btnScanner.setBackgroundResource(R.drawable.mybuttonclicked);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        btnScanner.setTextColor(Color.parseColor("#7a1a8b"));
                        btnScanner.setBackgroundResource(R.drawable.mybutton);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnScanner.setTextColor(Color.parseColor("#7a1a8b"));
                        btnScanner.setBackgroundResource(R.drawable.mybutton);
                        break;
                }

                return false;
            }
        });

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ID = String.valueOf(editId.getText());
                if (!aSwitch.isChecked()) {
                    status = "1";
                } else {
                    status = "0";
                }
                token = saveUserToken.getToken(MainActivity.this);
                time = String.valueOf(System.currentTimeMillis() / 1000);


                if (editId.getText().length() > 0 && !type.isEmpty() && isNetworkStatusAvialable(getApplicationContext())) {
                    Log.e("Время", "onSend: " + time);
                    if (!realm.isEmpty()) {

                        final List<Person> result = realm.where(Person.class).findAll();
                        final int size = result.size();

                        Log.e("SIZE", "size: " + size);
                        progressBar = new ProgressDialog(MainActivity.this);
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

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("BAZA", "run: " + result.get(finalI));
                                                person = result.get(finalI);
                                                SyncPost(person.getId(), person.getStatus(), person.getType(), person.getTime(), token, person);
                                                progressBar.setMessage("Отправлено - " + (finalI + 1));
                                            }
                                        });

                                        try {
                                            wait(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        i++;
                                    }
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (progressBar.isShowing()) {
                                            realm.beginTransaction();
                                            realm.deleteAll();
                                            realm.commitTransaction();
                                            Toast.makeText(MainActivity.this, "Успешная синхронизация", Toast.LENGTH_SHORT).show();
                                            progressBar.dismiss();
                                        }
                                    }
                                });
                            }
                        };
                        Thread thread = new Thread(runnable);
                        thread.start();

                        /*for (int i = 0; i < result.size(); i++) {

                            final int finalI = i;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.size() - 1 == finalI) {
                                        if (progressBar.isShowing()) {
                                            progressBar.dismiss();
                                            textViewFio.setText("");
                                            textViewAbout.setText("");
                                        }
                                    }
                                    final Person person;
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("BAZA", "onClick: " + result.get(finalI));
                                    person = result.get(finalI);
                                  //  SyncPost(person.getId(), person.getStatus(), person.getType(), person.getTime(), token, person);
                                }
                            }, 1000);
                        }*/
                    } else {

                        InfoPost(ID, status, type, time, token);
                        replace();
//                        progressBar = ProgressDialog.show(MainActivity.this, "Пожалуйста подождите", "Отправка...");
                    }

                } else {
                    if (editId.getText().length() == 0) {
                        editId.setError("Пустое поле.");
                        toast = Toast.makeText(getApplicationContext(), "Заполните все поля!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (type.isEmpty()) {
                        toast = Toast.makeText(getApplicationContext(), "Выберите Статус!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (!isNetworkStatusAvialable(getApplicationContext())) {
                        textViewFio.setText("Отсутствует Подключение к Сети");
                        textViewAbout.setText("Отсутствует Подключение к Сети");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Оффлайн")
                                .setMessage("Отсутствует подключение к сети, Запись будет добавлена в Базу данных")
                                //.setIcon(R.drawable.ic_android_cat)
                                .setCancelable(false)
                                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //  save_to_database(ID, status, type, time);

                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm bgRealm) {
                                                Person person = bgRealm.createObject(Person.class);
                                                person.setId(ID);
                                                person.setStatus(status);
                                                person.setType(type);
                                                person.setTime(time);
                                            }
                                        });
                                        Log.e("DATA_BASE", "onSuccess: " + "Успешно Добавлено в Базу");
                                        guestPhoto.setImageResource(R.drawable.add_icon);
                                        editId.setText(null);
                                        spinner.setSelection(0);
                                        fileToUpload = null;
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                }
            }
        });

        btnSendData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        btnSendData.setTextColor(Color.parseColor("#fdfdfe"));
                        btnSendData.setBackgroundResource(R.drawable.mybuttonclicked);
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        btnSendData.setTextColor(Color.parseColor("#7a1a8b"));
                        btnSendData.setBackgroundResource(R.drawable.mybutton);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnSendData.setTextColor(Color.parseColor("#7a1a8b"));
                        btnSendData.setBackgroundResource(R.drawable.mybutton);
                        break;
                }

                return false;
            }
        });

        guestPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentApiVersion = Build.VERSION.SDK_INT;

                if (currentApiVersion >= Build.VERSION_CODES.M) {
                    if (checkPermission() &&
                            ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            dispatchTakePictureIntent(CAMERA_REQUEST);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
                    } else {
                        checkPermissionsState();
                    }
                } else {
                    try {
                        dispatchTakePictureIntent(CAMERA_REQUEST);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void save_to_database(final String id, final String status, final String type, final String unixtime) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for (int i = 0; i < 100; i++) {

                    Person person = bgRealm.createObject(Person.class);
                    person.setId(id);
                    person.setStatus(status);
                    person.setType(type);
                    person.setTime(unixtime);
                }

            }
        });/*, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                // Transaction was a success.
                Log.e("DATA_BASE", "onSuccess: " + "Успешно Добавлено в Базу");
                guestPhoto.setImageResource(R.drawable.add_icon);
                editId.setText(null);
                spinner.setSelection(0);
                fileToUpload = null;
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("DATA_BASE", "onFailed: " + "Невозможно Добавить в Базу!!!!!!!!!!");
            }
        });*/
    }

    private void dispatchTakePictureIntent(int b) throws IOException {
        /*if (Build.VERSION.SDK_INT > 25) {
            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            file3 = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file3);
            startActivityForResult(captureIntent, b);
        } else {*/
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                file3 = null;
                Log.e("GHJKL:", "sdfeurif");
                try {
                    file3 = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("ERROR", "FILE");
                    return;
                }
                // Continue only if the File was successfully created
                if (file3 != null) {
                    // Uri photoURI = Uri.fromFile(createImageFile());
                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            file3);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, b);
                }
            }
        }
    //}

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "jj" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
       // File file = new File(Environment.getExternalStorageDirectory(), imageFileName);

        return image;
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Glide.with(MainActivity.this).load(file3).into(guestPhoto);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file3);
            fileToUpload = MultipartBody.Part.createFormData("foto", file3.getName(), mFile);
            Log.e("Photo", file3 + "");

        }
    }

    public void LogoutPost(String tokenn) {

        balAPI = App.getApi();

        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), tokenn);

        balAPI.logoutPost(token).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {

                Log.e("TAG", "message" + response.message());
                Log.e("TAG", "message" + response.body());


                if (response.code() == 401) {
                    if (progressExit.isShowing()) {
                        progressExit.dismiss();
                    }
                    saveUserToken.ClearToken(MainActivity.this);

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    if (progressExit.isShowing()) {
                        progressExit.dismiss();
                    }
                    saveUserToken.ClearToken(MainActivity.this);
                    Toast.makeText(MainActivity.this, response.body().getMess(), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }


            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
            }
        });
    }

    public void SyncPost(String idd, String statuss, String typee, String timee, String tokenn, final Person personn) {

        balAPI = App.getApi();

        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), idd);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), typee);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), statuss);
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), timee);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), tokenn);


        balAPI.infoPost(id, status, type, time, token, fileToUpload).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull final Response<TokenResponse> response) {

                Log.e("TAG", "message " + response.message());
                Log.e("TAG", "message " + response.body());

                if (response.body() != null) {
                    if (response.body().getStatus().equals("ok")) {
                        // progressBar.setMessage("Синхронизация... "+(size-1)+"/"+size);

                        /*if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }*/
                       /* realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                //Log.e("TAG", "Успешная синзронизация -> " + personn.getId());
                                //personn.deleteFromRealm();
                               *//* final List<Person> resultt = realm.where(Person.class).findAll();
                                resultt.size();
                                progressBar.setMessage("Осталось - "+resultt.size());*//*
                            }
                        });*/
                        textViewFio.setText("");
                        textViewAbout.setText("");

                        guestPhoto.setImageResource(R.drawable.add_icon);
                        editId.setText(null);
                        spinner.setSelection(0);
                        fileToUpload = null;
                        // Toast.makeText(MainActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(MainActivity.this, "Ошибка при синхронизации,данные не отправлены! Код= " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t);
            }
        });
    }

    public void InfoPost(String idd, String statuss, String typee, String timee, String tokenn) {

        balAPI = App.getApi();

        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), idd);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), typee);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), statuss);
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), timee);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), tokenn);


        balAPI.infoPost(id, status, type, time, token, fileToUpload).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {

                Log.e("TAG", "message " + response.message());
                Log.e("TAG", "message " + response.body());
                if (response.code() == 401) {
                    saveUserToken.ClearToken(MainActivity.this);
                    Toast.makeText(MainActivity.this, "Сессия устарела, Пожалуйста перезайдите", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("ok")) {

                            /*if (progressBar.isShowing()) {
                                progressBar.dismiss();
                            }
                            textViewFio.setText("");
                            textViewAbout.setText("");
                            if (checkBox.isChecked()) {
                                textViewFio.setText(response.body().getFio());
                                textViewAbout.setText(response.body().getAbout());
                            }
                            guestPhoto.setImageResource(R.drawable.add_icon);
                            editId.setText(null);
                            spinner.setSelection(0);
                            fileToUpload = null;*/
                            Toast.makeText(MainActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                        } else {
                            if (progressBar.isShowing()) {
                                progressBar.dismiss();
                            }
                            editId.setError("!");
                            textViewFio.setText(response.body().getMess());
                            textViewAbout.setText(response.body().getMess());
                            Toast.makeText(MainActivity.this, response.body().getMess(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(MainActivity.this, "Ошибка сервера,данные не отправлены! код= " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t);
            }
        });
    }

    public void replace(){
//        if (progressBar.isShowing()) {
//            progressBar.dismiss();
//        }
        textViewFio.setText("");
        textViewAbout.setText("");
//        if (checkBox.isChecked()) {
//            textViewFio.setText(response.body().getFio());
//            textViewAbout.setText(response.body().getAbout());
//        }
        guestPhoto.setImageResource(R.drawable.add_icon);
        editId.setText(null);
        spinner.setSelection(0);
        fileToUpload = null;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, CAMERA_REQUEST);
    }

    private void checkPermissionsState() {
        int cameraStatePermissionCheck = ContextCompat.checkSelfPermission(this,
                CAMERA);

        int writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int readExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);


        if (cameraStatePermissionCheck == PackageManager.PERMISSION_GRANTED &&
                writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED &&
                readExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.e("CheckPermission", "TRUE");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean somePermissionWasDenied = false;
                    for (int result : grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            somePermissionWasDenied = true;
                        }
                    }

                    if (somePermissionWasDenied) {
                        // Toast.makeText(this, "Cant load maps without all the permissions granted", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } else {
                    // Toast.makeText(this, "Cant load maps without all the permissions granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                if (netInfos.isConnected())
                    return true;
        }
        return false;
    }

}
