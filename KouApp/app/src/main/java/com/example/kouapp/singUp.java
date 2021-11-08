package com.example.kouapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class singUp extends AppCompatActivity {


        private ArrayList<String> uni = new ArrayList<>();
        private ArrayList<String> fac = new ArrayList<>();
        private ArrayList<String> sec = new ArrayList<>();
        String[] ClassArr = {"1","2","3","4","5","6","7","7+"};
        private ArrayAdapter<String> dataA1;
        private ArrayAdapter<String> dataA2;
        private ArrayAdapter<String> dataSec;
        private ArrayAdapter<String> AdapClas;
        Button Signup;
        Button SendImage;
        TextView studentNo;
        TextView tcNo;
        TextView name;
        TextView surname;
        TextView email;
        TextView phoneNo;
        TextView homeAdress;
        TextView businessAdress;
        EditText dateofbrith;
        Spinner universityName;
        Spinner facultyName;
        Spinner sectionName;
        Spinner Class;
        TextView password;

        String base64 = "";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sing_up);
                studentNo = findViewById(R.id.singUp_sudentNo);
                tcNo = findViewById((R.id.singUp_tcNo));
                name = findViewById((R.id.singUp_name));
                surname = findViewById((R.id.singUp_surname));
                email = findViewById((R.id.singUp_email));
                phoneNo = findViewById((R.id.singUp_phoneNo));
                homeAdress = findViewById((R.id.singUp_homeAddress));
                businessAdress = findViewById((R.id.singUp_businessAdress));
                dateofbrith = findViewById((R.id.singUp_dateofbrith));
                universityName = findViewById((R.id.singUp_universityName));
                facultyName = findViewById((R.id.singUp_facultyName));
                sectionName = findViewById((R.id.singUp_sectionName));
                Class = findViewById((R.id.singUp_classes));
                password = findViewById((R.id.singUp_password));

                AdapClas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, ClassArr);
                Class.setAdapter(AdapClas);
                Signup = findViewById(R.id.singUp_button);
                System.out.println("Signup");
                System.out.println(Signup.getText().toString());
                Spinner spin1 = findViewById(R.id.singUp_universityName);
                Signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(password.getText().toString().isEmpty()||tcNo.getText().toString().isEmpty()||
                                        name.getText().toString().isEmpty()||surname.getText().toString().isEmpty() || base64.isEmpty()){
                                        Server.MassageBox("HATA","Form Eksik Doldurulmuş",singUp.this);
                                }
                                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                String response = new String(responseBody, StandardCharsets.UTF_8);
                                                if(response == "True"){
                                                    Server.MassageBox("Başarılı", "Kayıt Başarılı",singUp.this);
                                                }
                                                else if (response == "False"){
                                                    Server.MassageBox("Başarısız", "Bu bilgilere sahip başka kayıt bulunmakta",singUp.this);
                                                }
                                                else {
                                                        Server.Failure("Başarısız",statusCode,headers,responseBody,singUp.this);
                                                }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                Server.Failure("Başarısız",statusCode,headers,responseBody,singUp.this);

                                        }
                                };
                                if(spin1.getSelectedItem().toString().equals("Diger"))
                                Server.Post(Server.addDatabaseRegistry,Server.AddDatabaseRegistry(
                                        studentNo.getText().toString(),
                                        tcNo.getText().toString(),
                                        name.getText().toString(),
                                        surname.getText().toString(),
                                        email.getText().toString(),
                                        phoneNo.getText().toString(),
                                        homeAdress.getText().toString(),
                                        businessAdress.getText().toString(),
                                        dateofbrith.getText().toString(),
                                        universityName.getSelectedItem().toString(),
                                        "",
                                        "",
                                        Class.getSelectedItem().toString(),
                                        password.getText().toString(),
                                        base64),responseHandler);
                               else
                                        Server.Post(Server.addDatabaseRegistry,Server.AddDatabaseRegistry(
                                                studentNo.getText().toString(),
                                                tcNo.getText().toString(),
                                                name.getText().toString(),
                                                surname.getText().toString(),
                                                email.getText().toString(),
                                                phoneNo.getText().toString(),
                                                homeAdress.getText().toString(),
                                                businessAdress.getText().toString(),
                                                dateofbrith.getText().toString(),
                                                universityName.getSelectedItem().toString(),
                                                facultyName.getSelectedItem().toString(),
                                                sectionName.getSelectedItem().toString(),
                                                Class.getSelectedItem().toString(),
                                                password.getText().toString(),
                                                base64),responseHandler);
                        }
                });


                //YENİ BUTON
                SendImage = findViewById(R.id.singUp_sendImage);
                SendImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                openfilechooser(v);
                        }
                });


                dataA1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, uni);

                Spinner spin2 = findViewById(R.id.singUp_facultyName);
                dataA2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text2, fac);
                spin2.setAdapter(dataA2);
                uni.add("Diger");
                uni.add("Kocaeli Üniversitesi");

                spin1.setAdapter(dataA1);
                spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int indeks, long l) {
                                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                String response = new String(responseBody, StandardCharsets.UTF_8);
                                                try {
                                                        fac.clear();
                                                        sec.clear();
                                                        spin2.clearAnimation();
                                                        sectionName.clearAnimation();
                                                        JSONObject json = new JSONObject(response);
                                                        for (int i= 1; i< json.length()+1;i++){
                                                               fac.add(json.getString(String.valueOf(i)));
                                                        }
                                                        spin2.setAdapter(dataA2);
                                                        sectionName.setAdapter(dataSec);
                                                } catch (JSONException e) {
                                                        Server.MassageBox("try catch", e.toString(),singUp.this);
                                                        e.printStackTrace();
                                                }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Server.Failure("onFailure",statusCode,headers,responseBody,singUp.this);
                                        }
                                };
                                if(spin1.getSelectedItem().toString().equals("Diger"))
                                        Server.Post(Server.databaseGetFacultyName,Server.DatabaseGetFacultyName("Dig"),responseHandler);
                                else    Server.Post(Server.databaseGetFacultyName,Server.DatabaseGetFacultyName("KOU"),responseHandler);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                });
                dataSec = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sec);
                sectionName.setAdapter(dataSec);
                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int indeks, long l) {
                                if(spin2.getSelectedItem().toString().length() < 1)return;
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
                                                        sectionName.setAdapter(dataSec);

                                                } catch (JSONException e) {
                                                        Server.MassageBox("try catch", e.toString(),singUp.this);
                                                        e.printStackTrace();
                                                }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                               Server.Failure("onFailure",statusCode,headers,responseBody,singUp.this);
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
                switch (facultyName.getSelectedItem().toString()){
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
                        InputStream inputStream;
                        try {
                                inputStream = getContentResolver().openInputStream(uri);
                                if(inputStream.available() <= 2097152){
                                        byte[] d = new byte[inputStream.available()];
                                        inputStream.read(d);
                                        base64 =  Base64.encodeToString(d, Base64.CRLF);
                                        base64 = base64.replace(" ","");
                                        base64 = base64.replace("\n","");
                                        byte[] tt = Base64.decode(base64,Base64.DEFAULT);

                                }
                                else{
                                        Server.MassageBox("HATA", "Dosya Çok Büyük Lütfen 2 MB dan küçük dosya seçiniz.",singUp.this);
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


