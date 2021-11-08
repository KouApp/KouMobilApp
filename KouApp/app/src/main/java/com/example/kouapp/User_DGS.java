package com.example.kouapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

public class User_DGS extends AppCompatActivity {

    Button dgs_eregister;
    Button dgs_courseContent;
    Button dgs_transkript;

    String fileName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dgs);

        dgs_eregister = findViewById(R.id.dgs_eregister_btn);
        dgs_eregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://www.turkiye.gov.tr/yok-universite-ekayit");
                Intent goWebsite = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(goWebsite);
            }
        });

        dgs_courseContent = findViewById(R.id.dgs_course_content_btn);
        dgs_courseContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName ="DersIcerikleri";
                openfilechooser(v);
            }
        });

        dgs_transkript = findViewById(R.id.dgs_transkript_btn);
        dgs_transkript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName ="transkript";
                openfilechooser(v);
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
            InputStream inputStream ;
            String value = "";
            try {
                inputStream = getContentResolver().openInputStream(uri);
                if(inputStream.available() <= 2097152){
                    byte[] d = new byte[inputStream.available()];
                    inputStream.read(d);
                    value =  Base64.encodeToString(d, Base64.DEFAULT);
                    value = value.replace(" ","");
                    value = value.replace("\n","");
                    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if(statusCode == 200)
                                Server.Failure("Başarılı", statusCode, headers, responseBody, User_DGS.this);
                            else
                                Server.Failure("HATA", statusCode, headers, responseBody, User_DGS.this);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Server.Failure("onFailure", statusCode, headers, responseBody, User_DGS.this);
                        }
                    };
                    Server.Post(Server.databaseSaveFile, Server.DatabaseSaveFile(SessionData.tc, mimeType, value, fileName,"DGS"), responseHandler);
                }
                else{
                    Server.MassageBox("HATA", "Dosya Çok Büyük Lütfen 2 MB dan küçük dosya seçiniz.",User_DGS.this);
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