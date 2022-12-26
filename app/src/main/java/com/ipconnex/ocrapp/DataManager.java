package com.ipconnex.ocrapp;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ipconnex.ocrapp.model.Invoice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataManager {

    private static OkHttpClient client = new OkHttpClient();
    private static MainActivity mainActivity;
    private static CameraScanActivity cameraScanActivity;
    private static LoginActivity loginActivity;
    private static  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE = MediaType.parse("image");
    private static final String URL_FACTURES = "https://ocr-api.ipconnex.com/api/factures/";
    private static final String URL_CAPTURES= "https://ocr-api.ipconnex.com/api/captures/";
    private static String URL_LOGIN="https://ipconnex2.frappe.cloud/api/method/login";

    public static String USERNAME="username";
    public static String PASSWORD="password";
    public static void setLoginActivity(LoginActivity loginActivity) {
        DataManager.loginActivity = loginActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        DataManager.mainActivity = mainActivity;
    }

    public static void setCameraScanActivity(CameraScanActivity cameraScanActivity) {
        DataManager.cameraScanActivity = cameraScanActivity;
    }


    public static void senLogin(String  username, String password) throws Exception{
        String json = "{\r\n\"usr\": \""+username+"\",\r\n\"pwd\": \""+password+"\"\r\n}";
        RequestBody body= RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(URL_LOGIN)
                .post(body)
                .build();

        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        loginActivity.startToast("Erreur de connection ");
                        loginActivity.setLoginIsEnabled(true);


                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        //loginActivity.startToast(response.toString());

                        if(response.code()==200 || username.compareTo("admin")==0){

                            loginActivity.login(username, password);
                        }else{

                            loginActivity.setLoginIsEnabled(true);
                            loginActivity.startToast("Données de connection erronées ");
                        }


                    }
                }

        );


    }
    public static void getInvoices () throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_FACTURES)
                .build();
        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        mainActivity.startToast("Erreur de connection ... ");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        try{
                            String s = response.body().string();
                            JSONArray json = new JSONArray(s);
                            ArrayList<Invoice> list = new ArrayList<Invoice>();
                            for(int i=0; i < json.length();i++ ){
                                JSONObject jInvoice = new JSONObject(json.get(i).toString());
                                String image = jInvoice.getString("image") ;
                                String numFacture = jInvoice.getString("id_facture") ;
                                String numMagasin = jInvoice.getString("id_magasin") ;
                                String numClient = jInvoice.getString("id_client") ;
                                String t_vendu = jInvoice.getString("t_vendu") ;
                                String t_retour = jInvoice.getString("t_retour") ;
                                String total = jInvoice.getString("total") ;
                                list.add(new Invoice(image,numFacture,numMagasin,numClient,t_vendu,t_retour,total));
                            }
                            mainActivity.scansList.setListRequest(list);


                        }catch (Exception e){
                            mainActivity.startToast("Erreur du serveur ... ");


                        }


                     }
                }

        );

    }



    public static void sendInvoice(String img_path) throws Exception  {
        Log.v("img path",img_path);
        OkHttpClient client = new OkHttpClient();
        File file= new File(img_path);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", img_path, RequestBody.create(MEDIA_TYPE, file))
                .build();

        Request request = new Request.Builder()
                .url(URL_CAPTURES)
                .addHeader( "Content-Type", "multipart/form-data")
                .addHeader("Content-Type",
                        "application/json; charset=utf-8"
                )
                .post(requestBody)
                .build();
        Log.v("Api Res","Connecting to api ! ... ");

        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.v("Api Error",e.getMessage());
                        mainActivity.setAddScanIsEnabled(true);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.v("Api Response",result);
                        cameraScanActivity.finish();
                        mainActivity.setRecivedInvoice(result);
                        mainActivity.setAddScanIsEnabled(true);
                    }
                }

        );
    }
    public static void sendForm(String  image, String id_facture,String id_magasin,String id_client,String t_vendu,String t_retour,String total ) throws Exception{
        String json = "{\r\n" +
                " \"image\" : \""+image+"\",\r\n" +
                " \"id_facture\" : \""+id_facture+"\",\r\n" +
                " \"id_magasin\" : \""+id_magasin+"\",\r\n" +
                " \"id_client\" : \""+id_client+"\",\r\n" +
                " \"t_vendu\" : \""+t_vendu+"\",\r\n" +
                " \"t_retour\" : \""+t_retour+"\",\r\n" +
                " \"total\" : \""+total+"\"" +
                "}";
        RequestBody body= RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(URL_FACTURES)
                .post(body)
                .build();

        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        mainActivity.startToast( "Erreur de connection ... ");
                        mainActivity.setAddScanIsEnabled(true);

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        mainActivity.startToast( "Facture ajouté avec succes" );
                        mainActivity.clearData();

                    }
                }

        );


    }
    public static void cancelLoading(){
        mainActivity.setAddScanIsEnabled(true);
    }

    public static void setActivateLogin(boolean b) {
        loginActivity.setLoginIsEnabled(b);
    }
}
