package com.example.kouapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;

public class ForgetPassword extends AppCompatActivity {

    Button resetPasswordButton;
    TextView studentNo, tcNo, phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        studentNo = findViewById(R.id.forget_sudentNo);
        tcNo = findViewById(R.id.forget_tcNo);
        phoneNo = findViewById(R.id.forget_phoneNo);
        resetPasswordButton = findViewById(R.id.forget_button);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);
                        if(response == "True"){
                            Server.MassageBox("Başarılı","Şifreniz öğrenci numaranız",ForgetPassword.this);
                        }
                        else if (response == "False"){
                            Server.MassageBox("Başarısız","Girilen bilgiler uyuşmuyor",ForgetPassword.this);
                        }
                        else{
                            Server.Failure("Başarısız",statusCode,headers,responseBody,ForgetPassword.this);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("Başarısız",statusCode,headers,responseBody,ForgetPassword.this);
                    }
                };
                Server.Post(Server.resetPassword,Server.ResetPassword(studentNo.getText().toString(),tcNo.getText().toString(),phoneNo.getText().toString()),responseHandler);
            }
        });
    }
}