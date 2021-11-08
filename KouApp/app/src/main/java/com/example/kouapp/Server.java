package com.example.kouapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import reactor.util.annotation.NonNull;

class BASE{
    public static String BASE_URL = "http://172.104.152.183:5000/";
    public static String Jenkins = "http://172.104.152.183:8080/";
    public static String CouchDB = "http://172.104.152.183:5984/";
}
public class Server {


    //USER
    public static String addDatabaseRegistry = "DatabaseRegistry";
    public static String loginUsers = "DatabaseLogin";
    public static String databaseGetFacultyName = "DatabaseGetFacultyName";
    public static String databaseGetSectionName = "DatabaseGetSectionName";
    public static String resetPassword = "DatabasePasswordReset";
    public static String databaseSaveFile = "DatabaseSaveFile";
    public static String databaseGetinfo = "DatabaseSaveFile";
    public static String databaseGetUsers = "DatabaseGetUsers";

    public static String yatayGecisBasvurusu = "YatayGecisBasvurusu";
    public static String databaseGetCap = "DatabaseGetCap";
    public static String capBasvurusu = "CapBasvurusu";
    public static String muafiyetBasvurusu = "MuafiyetBasvurusu";
    public static String yazOkuluBasvurusu = "YazOkuluBasvurusu";


    //ADMIN
    public static String databaseAdminUpdatefile = "DatabaseAdminUpdatefile";
    public static String databaseGetApplication = "DatabaseGetApplication";



    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void Post(String addition, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(BASE.BASE_URL + addition, params, responseHandler);
    }

    //USER
    public static RequestParams AddDatabaseRegistry(String studentNo, String tcNo, String name, String surname, String email, String phoneNo, String homeAddress, String businessAdress,
                                                    String dateofbrith, String universityName, String facultyName, String sectionName, String Class, String password,String ProfilePhotoBase64) {
        RequestParams params = new RequestParams();
        params.put("StudentNo", studentNo);
        params.put("TCNo", tcNo);
        params.put("Name", name);
        params.put("Surname", surname);
        params.put("Email", email);
        params.put("PhoneNo", phoneNo);
        params.put("HomeAddress", homeAddress);
        params.put("BusinessAddress", businessAdress);
        params.put("DateOfBrith", dateofbrith);
        params.put("UniversityName", universityName);
        params.put("DepartmanName", facultyName);
        params.put("SectionName", sectionName);
        params.put("Rate", Class);
        params.put("Password", password);
        params.put("ProfilePhotoBase64", ProfilePhotoBase64);
        return params;
    }
    public static RequestParams DatabaseGetUsers(String TCNo){
        return new RequestParams("TCNo",TCNo);
    }
    public static RequestParams Login(String TCNo, String password) {
        RequestParams params = new RequestParams();
        params.put("TCNo", TCNo);
        params.put("Password", password);
        return params;
    }
    public static RequestParams DatabaseGetFacultyName(String uni){
        return  new RequestParams("Abbreviation", uni);
    }
    public static RequestParams DatabaseGetSectionName(String faculity){
        return  new RequestParams("Abbreviation", faculity);
    }
    public static RequestParams ResetPassword(String studentNo, String tcNo, String phoneNo) {
        RequestParams params = new RequestParams();
        params.put("studentNo", studentNo);
        params.put("tcNo", tcNo);
        params.put("phoneNo", phoneNo);
        return params;
    }
    public static RequestParams DatabaseSaveFile(String TCNo, String fileType,String Base64, String fileName, String Purpose) {
        RequestParams params = new RequestParams();
        params.put("TCNo", TCNo);
        params.put("fileType", fileType);
        params.put("Base64", Base64);
        params.put("fileName", fileName);
        params.put("Purpose", Purpose);
        return params;
    }
    public static RequestParams DatabaseGetinfo(String TCNo)
    {
        return new RequestParams("TCNo",TCNo);
    }

    //APP
    public static RequestParams YatayGecisBasvurusu(String KurumYG,String KurumArasıYG,String MerYerPuanYG,String YurtDisiYG,String AdSoyad,String TCno,String DogumTarihi,
                                                    String Eposta,String GsmTel,String EvTel,String TebligatAdres,String KayitliUniversite,String KayitliFakulte,
                                                    String KayitliBolum,String birinciOgretim,String ikinciOgretim,String SınıfYarıyıl,String DisiplinCezası,String NotOrt,
                                                    String OgrenciNo,String KayitliYil,String KayitliPuan,String YabancıDilPuan,String BasvurFakulte,
                                                    String BasvurBolum,String BasvurBirinciOgr,String BasvurikinciOgr,String BasvurPuan,String Tarih){
        RequestParams requestParams = new RequestParams();
        requestParams.put("KurumYG",KurumYG);
        requestParams.put("KurumArasıYG",KurumArasıYG);
        requestParams.put("MerYerPuanYG",MerYerPuanYG);
        requestParams.put("YurtDisiYG",YurtDisiYG);
        requestParams.put("AdSoyad",AdSoyad);
        requestParams.put("TCno",TCno);
        requestParams.put("DogumTarihi",DogumTarihi);
        requestParams.put("Eposta",Eposta);
        requestParams.put("GsmTel",GsmTel);
        requestParams.put("EvTel",EvTel);
        requestParams.put("TebligatAdres",TebligatAdres);
        requestParams.put("KayitliUniversite",KayitliUniversite);
        requestParams.put("KayitliFakulte",KayitliFakulte);
        requestParams.put("KayitliBolum",KayitliBolum);
        requestParams.put("birinciOgretim",birinciOgretim);
        requestParams.put("ikinciOgretim",ikinciOgretim);
        requestParams.put("SınıfYarıyıl",SınıfYarıyıl);
        requestParams.put("DisiplinCezası",DisiplinCezası);
        requestParams.put("NotOrt",NotOrt);
        requestParams.put("OgrenciNo",OgrenciNo);
        requestParams.put("KayitliYil",KayitliYil);
        requestParams.put("KayitliPuan",KayitliPuan);
        requestParams.put("YabancıDilPuan",YabancıDilPuan);
        requestParams.put("BasvurFakulte",BasvurFakulte);
        requestParams.put("BasvurBolum",BasvurBolum);
        requestParams.put("BasvurBirinciOgr",BasvurBirinciOgr);
        requestParams.put("BasvurikinciOgr",BasvurikinciOgr);
        requestParams.put("BasvurPuan",BasvurPuan);
        requestParams.put("Tarih",Tarih);
        return  requestParams;
    }
    public static RequestParams DatabaseGetCap(String Abbreviation){
        return new RequestParams("SectionName",Abbreviation);
    }
    public static RequestParams CapBasvurusu(String BolumBaskanlik,String Fakulte,String Bolumu,String Program,String Ogretim,String OgrNo,
                                             String AdSoyad,String Bolumune,String Sinif,String GsmTel,String Email,String Adres){
        RequestParams requestParams = new RequestParams();
        requestParams.put("BolumBaskanlik",BolumBaskanlik);
        requestParams.put("Fakulte",Fakulte);
        requestParams.put("Bolumu",Bolumu);
        requestParams.put("Program",Program);
        requestParams.put("Ogretim",Ogretim);
        requestParams.put("OgrNo",OgrNo);
        requestParams.put("AdSoyad",AdSoyad);
        requestParams.put("Bolumune",Bolumune);
        requestParams.put("Sinif",Sinif);
        requestParams.put("GsmTel",GsmTel);
        requestParams.put("Email",Email);
        requestParams.put("Adres",Adres);
        return requestParams;
    }
    public static RequestParams MuafiyetBasvurusu(String Bolum,String Fakulte,String Yil,String AdSoyad,String GecisYolu,
                                            String Yariyil,String OgrNo,String intibakYariyil){
        RequestParams requestParams = new RequestParams();
        requestParams.put("Bolum",Bolum);
        requestParams.put("Fakulte",Fakulte);
        requestParams.put("Yil",Yil);
        requestParams.put("AdSoyad",AdSoyad);
        requestParams.put("GecisYolu",GecisYolu);
        requestParams.put("Yariyil",Yariyil);
        requestParams.put("OgrNo",OgrNo);
        requestParams.put("intibakYariyil",intibakYariyil);
        return requestParams;
    }
    public static RequestParams YazOkuluBasvurusu(String Baskanlik,String Fakulte,String Bolum,String OgrNo,String AdSoyad,
                                                  String YazUni,String YazFakulte,String BolumSinif,String GsmTel, String Email,String TebligatAdres){
        RequestParams requestParams = new RequestParams();
        requestParams.put("Baskanlik",Baskanlik);
        requestParams.put("Fakulte",Fakulte);
        requestParams.put("Bolum",Bolum);
        requestParams.put("OgrNo",OgrNo);
        requestParams.put("AdSoyad",AdSoyad);
        requestParams.put("YazUni",YazUni);
        requestParams.put("BolumSinif",BolumSinif);
        requestParams.put("GsmTel",GsmTel);
        requestParams.put("Email",Email);
        requestParams.put("TebligatAdres",TebligatAdres);
        return requestParams;
    }

//ADMIN
public static RequestParams DatabaseAdminUpdatefile(String TCNo,String purpose,String control)
{
    RequestParams params = new RequestParams();
    params.put("TCNo", TCNo);
    params.put("Purpose", purpose);
    params.put("control", control);
    return params;
}
    public static RequestParams DatabaseGetApplication(String TCNo, String password, String abbreviation, String UserTC)
    {
        RequestParams params = new RequestParams();
        params.put("TCNo", TCNo);
        params.put("Password", password);
        params.put("Abbreviation", abbreviation);
        params.put("UserTC", UserTC);
        return params;
    }


//OUTPUT
    public static void Failure(String title, int statusCode, Header[] headers, byte[] responseBody, @NonNull android.content.Context context) {
        String response = new String(responseBody, StandardCharsets.UTF_8);
        System.out.println("statusCode: " + statusCode + "\nHeader: " + headers[0] + "\nresponseBody: " + response);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage("statusCode: " + statusCode + "\nHeader: " + headers[0] + "\nresponseBody: " + response);
        builder.setPositiveButton("TAMAM", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void MassageBox(String title, String massage, @NonNull android.content.Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(massage);
        builder.setPositiveButton("TAMAM", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /*
    *
    *
    *
    * //kaynak https://www.youtube.com/watch?v=go5BdWCKLFk

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
        intent.setType("*//*");
    startActivityForResult(intent,requestcode);
    }*/
}
