package com.example.kouapp;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import cz.msebera.android.httpclient.Header;

public class AdminCategory extends AppCompatActivity {


    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> App;

    String[] basvurular = {"ÇAP","DGS","Yatay Geçiş","Yaz Okulu","İntibak İşlemleri"};
    Button search;
    Button accept;
    Button reject;
    RadioGroup rgAppState;
    RadioButton waiting;
    RadioButton others;
    RadioGroup rgOrders;
    RadioButton inorder;
    RadioButton preorder;
    RadioButton postorder;
    TextView adminName;
    ListView applications;
    EditText userTcno;

    JSONObject[] jsonObjects;

    //.
    String searchType = "";
    String purpose = "";
    String selectedTCNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        rgAppState = findViewById(R.id.admin_rg_appstate);
        rgOrders = findViewById(R.id.admin_rg_orders);
        inorder = findViewById(R.id.admin_rb_inorder);
        preorder = findViewById(R.id.admin_rb_preorder);
        postorder = findViewById(R.id.admin_rb_postorder);
        adminName = findViewById(R.id.admin_text_name); // adminin adı buraya gelecek
        applications = findViewById(R.id.admin_list_apps); // başvurular
        userTcno = findViewById(R.id.admin_text_usertcno);

        adminName.setText(SessionData.tc);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, basvurular);

        Spinner spinner = (Spinner)findViewById(R.id.admin_app_type);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                purpose = spinner.getSelectedItem().toString();
                for (int i = 0;i <basvurular.length;i++){
                    if(basvurular[i] == purpose){
                        switch (i){
                            case 0: purpose = "CAP"; break; //ÇAP
                            case 1: purpose = "DGS"; break; //DGS
                            case 2: purpose = "YG"; break;  //Yatay Geçiş
                            case 3: purpose = "YO"; break;  //Yaz Okulu
                            case 4: purpose = "DI"; break;  //İntibak İşlemleri
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        App=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        applications.setAdapter(App);
        search = findViewById(R.id.admin_button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FindSearchType();
                    GetApplications(userTcno.getText().toString());
            }
        });
        accept = findViewById(R.id.admin_button_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTCNo.isEmpty()) return;
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Server.MassageBox("Başarılı","İşlem Başarılı",AdminCategory.this);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("onFailure", statusCode, headers, responseBody, AdminCategory.this);
                    }
                };
                Server.Post(Server.databaseAdminUpdatefile,Server.DatabaseAdminUpdatefile(selectedTCNo,purpose,"ONAYLI"),responseHandler);
            }
        });
        reject = findViewById(R.id.admin_button_reject);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTCNo.isEmpty()) return;
                AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Server.MassageBox("Success","İşlem Başarılı",AdminCategory.this);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Server.Failure("onFailure", statusCode, headers, responseBody, AdminCategory.this);
                    }
                };
                Server.Post(Server.databaseAdminUpdatefile,Server.DatabaseAdminUpdatefile(selectedTCNo,purpose,"ONAYSIZ"),responseHandler);
            }
        });
        applications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String base64 ="";
                    try {
                       base64 =  jsonObjects[position].getString("Base64").toString();
                    }catch (Exception e){
                        System.out.println(e);
                        selectedTCNo = jsonObjects[position].getString("TC no");
                        GetApplications(selectedTCNo);
                        return;
                    }

                    String FileName = selectedTCNo+"_"+ jsonObjects[position].getString("Dosya ismi");
                    byte[] data = new byte[1];

                    try {
                        data = Base64.getDecoder().decode(base64);
                    }
                    catch (Exception e){
                        System.out.println(e);
                        return;
                    }

                    try (OutputStream stream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+FileName+".pdf")) {

                        stream.write(data);
                        Server.MassageBox("Başarılı","Dosya Kayıt Edildi\n"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+FileName+".pdf",AdminCategory.this);
                        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+FileName+".pdf");
                        //Intent target = new Intent(Intent.ACTION_VIEW);
                        //target.setDataAndType(Uri.fromFile(file),"application/pdf");
                        //target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        //Intent intent = Intent.createChooser(target, "Open File");
                        //startActivity(intent);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Server.MassageBox("test",e.toString(),AdminCategory.this);
                    } catch (IOException e) {
                        Server.MassageBox("test",e.toString(),AdminCategory.this);
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void FindSearchType(){
        if(inorder.isChecked()){
            searchType = "inorder";
        }
        else if(preorder.isChecked()){
            searchType = "preorder";
        }
        else if (postorder.isChecked()){
            searchType = "postorder";
        }
    }

    private String JSON2STR(JSONObject object){
        System.out.println("JSON2STR");
        String str ="";
        try {
            try {
                str += "TC No : "+object.getString("TC no");
            }catch (Exception e){
                str += "TC No : "+selectedTCNo;
            }

            str += "\nKontrol : "+object.getString("Kontrol");
            str += "\nDosya İsmi : "+object.getString("Dosya ismi");
        } catch (JSONException e) {
            System.out.println(e.toString());
            e.printStackTrace();

        }
        return str;
    }
    public void GetApplications(String TC){
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                System.out.println("response : "+response);
                try {
                    listItems.clear();
                    JSONObject json = new JSONObject(response);
                    jsonObjects = new JSONObject[json.length()];
                    System.out.println("jsonObjects");
                    for (int i= 1; i< json.length()+1;i++){
                        jsonObjects[i-1] =json.getJSONObject(String.valueOf(i));
                        listItems.add(JSON2STR(json.getJSONObject(String.valueOf(i))));
                    }
                    System.out.println("applications");
                    applications.setAdapter(App);
                    System.out.println("applications");

                } catch (JSONException e) {
                    Server.MassageBox("try catch", e.toString(),AdminCategory.this);
                    e.printStackTrace();
                }
                System.out.println("applications");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Server.Failure("onFailure",statusCode,headers,responseBody,AdminCategory.this);
            }
        };
        Server.Post(Server.databaseGetApplication,Server.DatabaseGetApplication(SessionData.tc,SessionData.password,purpose,TC),responseHandler);
    }
}