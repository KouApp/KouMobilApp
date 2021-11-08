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

public class User_CAP extends AppCompatActivity {

    private ArrayList<String> fac = new ArrayList<>();
    private ArrayList<String> sec = new ArrayList<>();
    private ArrayList<String> tarsec = new ArrayList<>();
    private ArrayAdapter<String> dataA1;
    private ArrayAdapter<String> dataA2;
    private ArrayAdapter<String> datatarsec;

    TextView cap_name;
    TextView cap_studentNo;
    TextView cap_program;
    TextView cap_educationType;
    TextView cap_phone;
    TextView cap_email;
    TextView cap_adress;
    Spinner cap_targetSection;
    Spinner cap_faculty;
    Spinner cap_section;
    Button cap_apply;
    Button cap_transkript;
    Button cap_sendFile;

    String fileName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cap);

        cap_name = findViewById(R.id.cap_name_surname_txt);
        cap_studentNo = findViewById(R.id.cap_student_no_txt);
        cap_program = findViewById(R.id.cap_program_txt);
        cap_educationType = findViewById(R.id.cap_education_type_txt);
        cap_phone = findViewById(R.id.cap_phone_txt);
        cap_email = findViewById(R.id.cap_email_txt);
        cap_adress = findViewById(R.id.cap_adress_txt);

        cap_apply = findViewById(R.id.cap_apply_btn);
        cap_targetSection = findViewById(R.id.cap_target_section_spin);
        datatarsec = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, tarsec);
        cap_targetSection.setAdapter(datatarsec);

        Spinner spin1 = findViewById(R.id.cap_faculty_spin);
        cap_faculty = findViewById(R.id.cap_faculty_spin);
        dataA1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, fac);
        spin1.setAdapter(dataA1);

        Spinner spin2 = findViewById(R.id.cap_section_spin);
        dataA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text2, sec);
        spin2.setAdapter(dataA2);
        cap_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String base64 = new String(responseBody, StandardCharsets.UTF_8);
                        try {
                            JSONObject object = new JSONObject(base64);
                            base64  = object.getString("base64");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(base64.equals("Kayitli")) {
                            Server.MassageBox("Kayıt Mevcut","Kayıt Mecvut",User_CAP.this);
                            return;
                        }
                        byte[] data = Base64.decode(base64,Base64.DEFAULT);
                        try (OutputStream stream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/CiftAdadalBasvurusu.pdf")) {
                            stream.write(data);
                            Server.MassageBox("Başarılı",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/CiftAdadalBasvurusu.pdf",User_CAP.this);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Server.MassageBox("catch",e.toString(),User_CAP.this);
                        } catch (IOException e) {
                            Server.MassageBox("catch",e.toString(),User_CAP.this);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("onFailure", statusCode, headers, responseBody, User_CAP.this);
                    }

                };
                Server.Post(Server.capBasvurusu,Server.CapBasvurusu(spin2.getSelectedItem().toString(),spin1.getSelectedItem().toString(),spin2.getSelectedItem().toString(),
                        cap_program.getText().toString(),cap_educationType.getText().toString(),cap_studentNo.getText().toString(),cap_name.getText().toString(),
                        cap_targetSection.getSelectedItem().toString(),"",cap_phone.getText().toString(),cap_email.getText().toString(),cap_adress.getText().toString()),responseHandler);
            }
        });

        cap_transkript = findViewById(R.id.cap_transkript_btn);
        cap_transkript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = "transkript";
                openfilechooser(v);
            }
        });

        cap_sendFile = findViewById(R.id.cap_send_file_btn);
        cap_sendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileName = "CAPBasvuru";
                openfilechooser(v);
            }
        });



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
                    Server.MassageBox("try catch", e.toString(),User_CAP.this);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Server.Failure("onFailure",statusCode,headers,responseBody,User_CAP.this);
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
                            Server.MassageBox("try catch", e.toString(),User_CAP.this);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("Failure",statusCode,headers,responseBody,User_CAP.this);
                    }
                };
                Server.Post(Server.databaseGetSectionName,Server.DatabaseGetSectionName(GetFaculty()),responseHandler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int indeks, long l) {
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        try {
                            tarsec.clear();
                            JSONObject json = new JSONObject(response);
                            for (int i= 1; i< json.length()+1;i++){
                                tarsec.add(json.getString(String.valueOf(i)));
                            }
                            cap_targetSection.setAdapter(datatarsec);

                        } catch (JSONException e) {
                            Server.MassageBox("try catch", e.toString(),User_CAP.this);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("Failure",statusCode,headers,responseBody,User_CAP.this);
                    }
                };
                System.out.println("#"+spin2.getSelectedItem().toString().trim()+"#");
                Server.Post(Server.databaseGetCap,Server.DatabaseGetCap(spin2.getSelectedItem().toString().trim()),responseHandler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private String GetFaculty(){
        switch (cap_faculty.getSelectedItem().toString()){
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
                                Server.Failure("Başarılı", statusCode, headers, responseBody, User_CAP.this);
                            else
                                Server.Failure("HATA", statusCode, headers, responseBody, User_CAP.this);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Server.Failure("onFailure", statusCode, headers, responseBody, User_CAP.this);
                        }
                    };
                    Server.Post(Server.databaseSaveFile, Server.DatabaseSaveFile(SessionData.tc, mimeType, value, fileName,"CAP"), responseHandler);

                }
                else{
                    Server.MassageBox("HATA", "Dosya Çok Büyük Lütfen 2 MB dan küçük dosya seçiniz.",User_CAP.this);
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