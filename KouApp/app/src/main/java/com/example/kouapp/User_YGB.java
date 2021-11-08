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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;

public class User_YGB extends AppCompatActivity {
    String[] BasvuruTuru = {"KURUMİÇİ YATAY GEÇİŞ BAŞVURUSU","KURUMLARARASI YATAY GEÇİŞ BAŞVURUSU","MER. YER. PUANIYLA YATAY GEÇİŞ BAŞVURUSU","YURT DIŞI YATAY GEÇİŞ BAŞVURUSU"};
    String[] EduType = {"I.Ogretim","II.Ogretim"};

    Spinner ygb_appType;
    Spinner ygb_educationType;
    TextView ygb_nameSurname;
    TextView ygb_tcNo;
    TextView ygb_birthDate;
    TextView ygb_phone;
    TextView ygb_email;
    TextView ygb_adress;
    TextView ygb_registeredUni;
    TextView ygb_registeredFaculty;
    TextView ygb_registeredSection;
    TextView ygb_studentNo;
    TextView ygb_registeredYear;
    TextView ygb_appliedFaculty;
    TextView ygb_appliedSection;
    TextView ygb_homePhone;
    TextView ygb_grade;
    TextView ygb_semester;
    TextView ygb_averageGrade;
    TextView ygb_applicationScore;
    TextView ygb_registeredScore;
    TextView ygb_date;
    TextView ygb_language;
    TextView ygb_applicationEduType;
    TextView ygb_disciplinaryTxt;
    Button ygb_apply;
    Button ygb_transkript;
    Button ygb_disciplinary;
    Button ygb_osym_result;
    Button ygb_receipt;

    String fileName ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ygb);
        ygb_appType = findViewById(R.id.ygb_app_type_spinner);
        ygb_educationType = findViewById(R.id.ygb_education_type_spinner);
        ygb_nameSurname = findViewById(R.id.ygb_name_surname_txt);
        ygb_tcNo = findViewById(R.id.ygb_tcno_txt);
        ygb_birthDate = findViewById(R.id.ygb_birth_date_txt);
        ygb_phone = findViewById(R.id.ygb_phone_txt);
        ygb_email = findViewById(R.id.ygb_email_txt);
        ygb_adress = findViewById(R.id.ygb_adress_txt);
        ygb_registeredUni = findViewById(R.id.ygb_registered_uni_txt);
        ygb_registeredFaculty = findViewById(R.id.ygb_registered_faculty_txt);
        ygb_registeredSection = findViewById(R.id.ygb_registered_section_txt);
        ygb_studentNo = findViewById(R.id.ygb_ogrno_txt);
        ygb_registeredYear = findViewById(R.id.ygb_registered_year_txt);
        ygb_appliedFaculty = findViewById(R.id.ygb_applied_faculty_txt);
        ygb_appliedSection = findViewById(R.id.ygb_applied_section_txt);
        ygb_homePhone = findViewById(R.id.ygb_home_phone_txt);
        ygb_grade = findViewById(R.id.ygb_current_grade_txt);
        ygb_semester = findViewById(R.id.ygb_current_semester_txt);
        ygb_averageGrade = findViewById(R.id.ygb_grade_avarage_txt);
        ygb_applicationScore = findViewById(R.id.ygb_application_score_txt);
        ygb_registeredScore = findViewById(R.id.ygb_registered_score_txt);
        ygb_date = findViewById(R.id.ygb_date_txt);
        ygb_language = findViewById(R.id.ygb_language_score_txt);
        ygb_applicationEduType = findViewById(R.id.ygb_application_education_type_txt);
        ygb_disciplinaryTxt = findViewById(R.id.ygb_disciplinary_txt);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BasvuruTuru);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EduType);
        ygb_appType.setAdapter(adapter);
        ygb_educationType.setAdapter(adapter2);

        //Yatay Geçiş Başvuru Butonu
        ygb_apply = findViewById(R.id.ygb_apply_btn);
        ygb_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String base64 = new String(responseBody, StandardCharsets.UTF_8);
                        if(base64.equals("Kayitli")) {
                            Server.MassageBox("Kayıt Mevcut","Kayıt Mecvut",User_YGB.this);
                            return;
                        }
                        byte[] data = Base64.decode(base64,Base64.DEFAULT);
                        try (OutputStream stream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/YatayGecisBasvurusu.pdf")) {
                            stream.write(data);
                            Server.MassageBox("Başarılı",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/YatayGecisBasvurusu.pdf",User_YGB.this);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Server.MassageBox("catch",e.toString(),User_YGB.this);
                        } catch (IOException e) {
                            Server.MassageBox("catch",e.toString(),User_YGB.this);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("onFailure", statusCode, headers, responseBody, User_YGB.this);
                    }
                };
                String KurumYG="",KurumArasıYG="",MerYerPuanYG="",YurtDisiYG="";
                switch(ygb_appType.getSelectedItemPosition()){
                    case 0: KurumYG = "X"; KurumArasıYG = "O";MerYerPuanYG = "O";YurtDisiYG = "O";break;
                    case 1: KurumYG = "O"; KurumArasıYG = "X";MerYerPuanYG = "O";YurtDisiYG = "O";break;
                    case 2: KurumYG = "O"; KurumArasıYG = "O";MerYerPuanYG = "X";YurtDisiYG = "O";break;
                    case 3: KurumYG = "O"; KurumArasıYG = "O";MerYerPuanYG = "O";YurtDisiYG = "X";break;
                }
                String BirinciO ="", IkinciO="";
                switch(ygb_educationType.getSelectedItemPosition()){
                    case 0: BirinciO = "X"; IkinciO = "O";break;
                    case 1: BirinciO = "O"; IkinciO = "X";break;
                }
                Server.Post(Server.yatayGecisBasvurusu,Server.YatayGecisBasvurusu(KurumYG,KurumArasıYG,MerYerPuanYG,YurtDisiYG,ygb_nameSurname.getText().toString(),ygb_tcNo.getText().toString(),
                        ygb_birthDate.getText().toString(),ygb_email.getText().toString(),ygb_phone.getText().toString(),ygb_phone.getText().toString(),ygb_adress.getText().toString(),
                        ygb_registeredUni.getText().toString(),ygb_registeredFaculty.getText().toString(),ygb_registeredSection.getText().toString(),BirinciO,IkinciO,ygb_semester.getText().toString(),ygb_disciplinaryTxt.getText().toString(),
                        ygb_averageGrade.getText().toString(),ygb_studentNo.getText().toString(),ygb_registeredYear.getText().toString(),ygb_registeredScore.getText().toString(),ygb_language.getText().toString(),
                        ygb_appliedFaculty.getText().toString(),ygb_appliedSection.getText().toString(),BirinciO,IkinciO,ygb_applicationScore.getText().toString(),ygb_date.getText().toString()),responseHandler);
            }
        });

        ygb_transkript = findViewById(R.id.ygb_transkript_btn);
        ygb_transkript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            fileName = "Transkript";
                openfilechooser(v);
            }
        });

        ygb_disciplinary = findViewById(R.id.ygb_disciplinary_btn);
        ygb_disciplinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = "Disiplin";
                openfilechooser(v);
            }
        });

        ygb_osym_result = findViewById(R.id.ygb_osym_result_btn);
        ygb_osym_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = "OSYMBelgesi";
                openfilechooser(v);
            }
        });

        ygb_receipt = findViewById(R.id.ygb_receipt_btn);
        ygb_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = "Dekont";
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
                            Server.Failure("Başarılı", statusCode, headers, responseBody, User_YGB.this);
                            else
                            Server.Failure("HATA", statusCode, headers, responseBody, User_YGB.this);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Server.Failure("onFailure", statusCode, headers, responseBody, User_YGB.this);
                        }
                    };
                    Server.Post(Server.databaseSaveFile, Server.DatabaseSaveFile(SessionData.tc, mimeType, value, fileName,"YGB"), responseHandler);
                }
                else{
                    Server.MassageBox("HATA", "Dosya Çok Büyük Lütfen 2 MB dan küçük dosya seçiniz.",User_YGB.this);
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