package com.example.kouapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class User_YAZ extends AppCompatActivity {

    private ArrayList<String> fac = new ArrayList<>();
    private ArrayList<String> sec = new ArrayList<>();
    private ArrayAdapter<String> dataA1;
    private ArrayAdapter<String> dataA2;

    TextView yaz_name;
    TextView yaz_studentNo;
    Spinner yaz_faculty;
    Spinner yaz_section;
    TextView yaz_phone;
    TextView yaz_email;
    TextView yaz_adress;
    TextView yaz_educationType;
    TextView yaz_targetUniversity;
    TextView yaz_targetFaculty;
    TextView yaz_head;//BAŞKANLIK
    Button yaz_apply;
    Button yaz_sendFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_yaz);

        yaz_name = findViewById(R.id.yaz_name_surname_txt);
        yaz_studentNo = findViewById(R.id.yaz_studentNo_txt);
        yaz_targetUniversity = findViewById(R.id.yaz_target_university_txt);
        yaz_targetFaculty = findViewById(R.id.yaz_target_faculty_txt);
        yaz_head = findViewById(R.id.yaz_head_txt);//BAŞKANLIK
        yaz_phone = findViewById(R.id.yaz_phone_txt);
        yaz_email = findViewById(R.id.yaz_email_txt);
        yaz_adress = findViewById(R.id.yaz_adress_txt);
        yaz_educationType = findViewById(R.id.yaz_education_type_txt);

        yaz_apply = findViewById(R.id.yaz_apply_btn);
        yaz_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String base64 = new String(responseBody, StandardCharsets.UTF_8);
                        if(base64.equals("Kayitli")) {
                            Server.MassageBox("Kayıt Mevcut","Kayıt Mecvut",User_YAZ.this);
                            return;
                        }
                        byte[] data = Base64.decode(base64,Base64.DEFAULT);
                        try (OutputStream stream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/YazOkuluBasvurusu.pdf")) {
                            stream.write(data);
                            Server.MassageBox("Başarılı",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/YazOkuluBasvurusu.pdf",User_YAZ.this);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Server.MassageBox("catch",e.toString(),User_YAZ.this);
                        } catch (IOException e) {
                            Server.MassageBox("catch",e.toString(),User_YAZ.this);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("onFailure", statusCode, headers, responseBody, User_YAZ.this);
                    }
                };
                Server.Post(Server.yazOkuluBasvurusu,Server.YazOkuluBasvurusu("",yaz_faculty.getSelectedItem().toString(),yaz_section.getSelectedItem().toString(),
                        yaz_studentNo.getText().toString(),yaz_name.getText().toString(),yaz_targetUniversity.getText().toString(),yaz_targetFaculty.getText().toString(),
                        "",yaz_phone.getText().toString(),yaz_email.getText().toString(),yaz_adress.getText().toString()),responseHandler);
            }
        });

        yaz_sendFile = findViewById(R.id.yaz_file_btn);
        yaz_sendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Spinner spin1 = findViewById(R.id.yaz_faculty_spin);
        yaz_faculty = findViewById(R.id.yaz_faculty_spin);
        dataA1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, fac);
        spin1.setAdapter(dataA1);

        Spinner spin2 = findViewById(R.id.yaz_section_spin);
        yaz_section = findViewById(R.id.yaz_section_spin);
        dataA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text2, sec);
        spin2.setAdapter(dataA2);

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                try {
                    JSONObject json = new JSONObject(response);
                    for (int i= 1; i< json.length()+1;i++){
                        fac.add(json.getString(String.valueOf(i)));
                    }
                    spin1.setAdapter(dataA1);

                } catch (JSONException e) {
                    Server.MassageBox("try catch", e.toString(),User_YAZ.this);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Server.Failure("onFailure",statusCode,headers,responseBody,User_YAZ.this);
            }
        };
        Server.Post(Server.databaseGetFacultyName,Server.DatabaseGetFacultyName("KOU"),responseHandler);


        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int indeks, long l) {
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        try {
                            sec.clear();
                            JSONObject json = new JSONObject(response);
                            for (int i= 1; i< json.length()+1;i++){
                                sec.add(json.getString(String.valueOf(i)));
                            }
                            spin2.setAdapter(dataA2);

                        } catch (JSONException e) {
                            Server.MassageBox("try catch", e.toString(),User_YAZ.this);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("Failure",statusCode,headers,responseBody,User_YAZ.this);
                    }
                };
                Server.Post(Server.databaseGetSectionName,Server.DatabaseGetSectionName(GetFaculty()),responseHandler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    private String GetFaculty(){
        switch (yaz_faculty.getSelectedItem().toString()){
            case "İletişim Fakültesi": return "IF";
            case "Mühendislik Fakültesi": return "MF";
            case "Fen-Edebiyat Fakültesi": return "FEF";
            case "İktisadi ve İdari Bilimler Fakültesi": return "IIF";
            case "Eğitim Fakültesi": return "EF";
            default: return "NULL";
        }
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
                                Server.Failure("Başarılı", statusCode, headers, responseBody, User_YAZ.this);
                            else
                                Server.Failure("HATA", statusCode, headers, responseBody, User_YAZ.this);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Server.Failure("onFailure", statusCode, headers, responseBody, User_YAZ.this);
                        }
                    };
                    Server.Post(Server.databaseSaveFile, Server.DatabaseSaveFile(SessionData.tc, mimeType, value, "YazOkuluBasvurusu","YAZ"), responseHandler);
                }
                else{
                    Server.MassageBox("HATA", "Dosya Çok Büyük Lütfen 2 MB dan küçük dosya seçiniz.",User_YAZ.this);
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