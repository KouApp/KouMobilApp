package com.example.kouapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    ConstraintLayout butonSignin;
    ConstraintLayout butonSignup;
    TextView butonReset;

    TextView studentNo,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentNo = findViewById(R.id.studentNoTextBox);
        password = findViewById(R.id.passwordTextBox);
        butonSignin = findViewById(R.id.L_signin_B);
        butonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        String admins = new String(responseBody,StandardCharsets.UTF_8);


                        if(response.equals("users")){
                            Intent ıntent = new Intent(MainActivity.this, UserMain.class);
                            startActivity(ıntent);
                        }
                        else if (response.equals("admins")){
                            Intent ıntent = new Intent(MainActivity.this, AdminCategory.class);
                            startActivity(ıntent);
                        }
                        else if (response.equals("False")){
                            Server.MassageBox("Başarısız", "Yanlış öğrenci numarası şifre eşleşmesi",MainActivity.this);
                        }
                        else{
                            Server.Failure("Bilinmeyen bir sorun",statusCode,headers,responseBody,MainActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("Bağlantı Başarısız",statusCode,headers,responseBody,MainActivity.this);
                    }
                };
                SessionData.tc = studentNo.getText().toString();
                SessionData.password = password.getText().toString();
                Server.Post(Server.loginUsers,Server.Login(studentNo.getText().toString(),password.getText().toString()),responseHandler);

            }
        });

        butonSignup = findViewById(R.id.L_signup_B);
        butonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(MainActivity.this, singUp.class);
                startActivity(ıntent);
            }
        });
        butonReset = findViewById(R.id.L_reset_B);
        butonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });


    }

    //kaynak https://www.youtube.com/watch?v=go5BdWCKLFk

    int requestcode = 1;
    //Dosya Okuma İşlemleri
public void onActivityResult(int requestcode, int resulCode, Intent data){
        super.onActivityResult(requestcode,resulCode,data);
        Context context = getApplicationContext();
        if(requestcode == requestcode && resulCode == Activity.RESULT_OK){
            if(data == null){
                return;
            }
            Uri uri = data.getData();
            String mimeType = getContentResolver().getType(uri);
            Server.MassageBox("test",mimeType,MainActivity.this);


            InputStream inputStream ;
            String value = "";
            try {
                inputStream = getContentResolver().openInputStream(uri);
                if(inputStream.available() <= 2097152){
                    byte[] d = new byte[inputStream.available()];
                    inputStream.read(d);
                    value =  Base64.encodeToString(d, Base64.DEFAULT);
                    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Server.Failure("onSuccess", statusCode, headers, responseBody, MainActivity.this);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Server.Failure("onFailure", statusCode, headers, responseBody, MainActivity.this);
                        }
                    };
                    Server.Post(Server.databaseSaveFile, Server.DatabaseSaveFile("1111", "pdf", value, "test","dgs"), responseHandler);
                }
                else{
                    Server.MassageBox("HATA", "Dosya Çok Büyük Lütfen 2 MB dan küçük dosya seçiniz.",MainActivity.this);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openfilechooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,requestcode);
    }

}