package com.example.kouapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
public class UserMain extends AppCompatActivity {

    TextView stuNo;
    TextView stuName;
    Button dgsBtn;
    Button ygbBtn;
    Button yazBtn;
    Button capBtn;
    Button intBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        stuNo = findViewById(R.id.user_main_no_text);
        stuNo.setText(SessionData.tc);
        stuName = findViewById(R.id.user_main_name_text);
        dgsBtn = findViewById(R.id.user_main_dgs_btn);
        AsyncHttpResponseHandler Asynsresponse = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                try {
                    JSONObject json = new JSONObject(response);
                    stuName.setText(json.getString("Name") +" " +json.getString("Surname"));
                    String base64 = json.getString("ProfilePhotoBase64");
                    System.out.println(base64);
                    byte[] data = Base64.decode(base64,Base64.DEFAULT);
                    ImageView img = findViewById(R.id.imageView3);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    img.setImageBitmap(bitmap);
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };
        Server.Post(Server.databaseGetUsers, Server.DatabaseGetUsers(SessionData.tc),Asynsresponse);
        dgsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(UserMain.this, User_DGS.class);
                startActivity(ıntent);
            }
        });
        ygbBtn = findViewById(R.id.user_main_ygb_btn);
        ygbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(UserMain.this, User_YGB.class);
                startActivity(ıntent);
            }
        });
        yazBtn = findViewById(R.id.user_main_yaz_btn);
        yazBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(UserMain.this, User_YAZ.class);
                startActivity(ıntent);
            }
        });
        capBtn = findViewById(R.id.user_main_cap_btn);
        capBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(UserMain.this, User_CAP.class);
                startActivity(ıntent);
            }
        });
        intBtn = findViewById(R.id.user_main_int_btn);
        intBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(UserMain.this, User_INT.class);
                startActivity(ıntent);
            }
        });
    }
}