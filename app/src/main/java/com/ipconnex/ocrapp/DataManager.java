package com.ipconnex.ocrapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ipconnex.ocrapp.model.Chargement;
import com.ipconnex.ocrapp.model.Invoice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;


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
    private static String URL_API="https://ipconnex2.frappe.cloud/api";
    private static String URL_LOGIN=URL_API+"/method/login";
    private static String URL_CHARGEMENT=URL_API+"/resource/RapportChargement";
    private static String URL_ACCESS=URL_API+"/resource/OCR-UserAcess/";
    private static String URL_DistributionNRoute=URL_API+"/resource/DistributionRoute/";
    private static String URL_LISTCHARGEMENTS=URL_API+"/resource/RapportChargement?fields=[\"route_id\",\"date\",\"products_list\",\"image\"]&limit_page_length=9999&order_by=date%20desc";
    private static String URL_FACTURE="https://ipconnex2.frappe.cloud/api/resource/Facture";
    private static String URL_LISTFACTURES="https://ipconnex2.frappe.cloud/api/resource/Facture?fields=[\"num_client\",\"num_magazin\",\"num_facture\",\"total\",\"total_vendu\",\"total_retour\",\"scan_facture\",\"qte\",\"date_facture\"]&limit_page_length=9999&order_by=date_facture%20desc";
    private static final String URL_CAPTURES= "https://ocr-api.ipconnex.com/api/captures/";
    private static final String URL_RAPPORTS= "https://ocr-api.ipconnex.com/api/chargement/";
    private static final String PDF_API= "https://ocr-api.ipconnex.com/pdf/";
    private static String image_path ="";
    public static String USERNAME="username";
    public static String PASSWORD="password";
    public static String SESSIONID="sid";
    public static String MAGASINS="magasin";
    public static String ROUTE="route";
    public static String routes="";
    public static String magasins="";
    public static void setLoginActivity(LoginActivity loginActivity) {
        DataManager.loginActivity = loginActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        DataManager.mainActivity = mainActivity;
    }

    public static void setCameraScanActivity(CameraScanActivity cameraScanActivity) {
        DataManager.cameraScanActivity = cameraScanActivity;
    }


    public static void sendLogin(String  username, String password) throws Exception{
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


                        if(response.code()==200 ){
                            List<String> Cookielist = response.headers().values("Set-Cookie");
                            String jsessionid = (Cookielist .get(0).split(";"))[0];
                            Log.v("Session",jsessionid);
                            try{
                                getAccess(username,jsessionid);
                            }catch (Exception e){
                                Log.v("Error","Error");

                            }

                            loginActivity.login(username, password,jsessionid);
                        }else{
                            loginActivity.setLoginIsEnabled(true);
                            loginActivity.startToast("Données de connection erronées ");
                        }

                    }
                }

        );


    }
    public static void getInvoices () throws Exception {
        SharedPreferences sp=mainActivity.getSharedPreferences("Login", mainActivity.MODE_PRIVATE);
        String sid = sp.getString(DataManager.SESSIONID,"");
        String user = sp.getString(DataManager.USERNAME,"");
        String magasin =sp.getString(DataManager.MAGASINS,"");
        String url= URL_LISTFACTURES;

        if(magasins!=""){
            url=url+"&filters=[[%22num_magazin%22,%22in%22,%22"+magasin+"%22]]";
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().addHeader( "Content-Type", "multipart/form-data")
                .addHeader("Authorization", "Bearer "+sid)
                .addHeader("Cookie", "full_name=API;"+sid+"; system_user=yes; user_id="+user+"; user_image=")
                .url(url)
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
                            Log.v("response",s);
                            JSONObject json = new JSONObject(s);
                            JSONArray data = new JSONArray(json.getString("data"));
                            Log.v("data",data.toString());
                            ArrayList<Invoice> list = new ArrayList<Invoice>();
                            for(int i=0; i <data.length() ;i++){
                                JSONObject jInvoice = new JSONObject(data.get(i).toString());//"num_client","num_magazin","num_facture","total","total_vendu","total_retour","scan_facture"
                                String image = jInvoice.getString("scan_facture") ;
                                Log.v("image",image);
                                String numFacture = jInvoice.getString("num_facture") ;
                                String numMagasin = jInvoice.getString("num_magazin") ;
                                String numClient = jInvoice.getString("num_client") ;
                                String t_vendu = jInvoice.getString("total_vendu") ;
                                String t_retour = jInvoice.getString("total_retour") ;
                                String total = jInvoice.getString("total") ;
                                String qte= jInvoice.getString("qte") ;
                                String date_facture = jInvoice.getString("date_facture") ;
                                list.add(new Invoice(image,numFacture,numMagasin,numClient,t_vendu,t_retour,total,date_facture,qte));
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
        image_path=img_path;
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
                        mainActivity.setImageText("Inv","Erreur ");
                        mainActivity.setAddScanIsEnabled(true);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String result = response.body().string();

                        mainActivity.setImageText("Inv","Capture prise");

                        Log.v("Api Response",result);
                        cameraScanActivity.finish();
                        mainActivity.setRecivedInvoice(result);
                        mainActivity.setAddScanIsEnabled(true);
                    }
                }

        );
    }
    public static void sendForm(String  image, String id_facture,String id_magasin,String id_client,String date_facture,String qte ,String t_vendu,String t_retour,String total) throws Exception{

        SharedPreferences sp=mainActivity.getSharedPreferences("Login", mainActivity.MODE_PRIVATE);
        String sid = sp.getString(DataManager.SESSIONID,"");
        String user = sp.getString(DataManager.USERNAME,"");
        Log.v("image",image_path);

        RequestBody body= new MultipartBody.Builder().setType(MultipartBody.FORM)

                .addFormDataPart("scan_facture",image)
                .addFormDataPart("nom_client",id_client)
                    .addFormDataPart("num_client",id_client)
                    .addFormDataPart("num_magazin",id_magasin)
                    .addFormDataPart("num_facture",id_facture)
                    .addFormDataPart("total",total)
                    .addFormDataPart("total_vendu",t_vendu)
                    .addFormDataPart("total_retour",t_retour)
                    .addFormDataPart("qte",qte)
                    .addFormDataPart("date_facture",date_facture)
                    .build();

        Request request = new Request.Builder()
                .url(URL_FACTURE).addHeader( "Content-Type", "multipart/form-data")
                .addHeader("Authorization", "Bearer "+sid)
                .addHeader("Cookie", "full_name=API;"+sid+"; system_user=yes; user_id="+user+"; user_image=")
                .post(body)
                .build();


        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        mainActivity.startToast( "Erreur de connection ... ");
                        Log.v("response ",e.toString());
                        mainActivity.setAddScanIsEnabled(true);
                        mainActivity.clearData();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.v("response ",response.toString());
                        if(response.code()==200){
                            DataManager.image_path="";
                            mainActivity.startToast( "Facture ajouté avec succes" );
                            mainActivity.clearData();
                        }else{
                            mainActivity.startToast( "Erreur lors de l'envoi de la facture !" );
                            mainActivity.setAddScanIsEnabled(true);

                        }

                    }
                }

        );


    }
    public static void sendRapport(String img_path) {
        Log.v("img path",img_path);
        OkHttpClient client = new OkHttpClient();
        File file= new File(img_path);
        image_path=img_path;
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", img_path, RequestBody.create(MEDIA_TYPE, file))
                .build();

        Request request = new Request.Builder()
                .url(URL_RAPPORTS)
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
                        mainActivity.setImageText("Rap","Erreur ");
                        mainActivity.setChargementIsEnabled(true);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String result = response.body().string();

                        Log.v("Api Response",result);
                        cameraScanActivity.finish();
                        mainActivity.setRecivedRapport(result);
                        mainActivity.setImageText("Rap","Capture prise");
                        mainActivity.setChargementIsEnabled(true);
                    }
                }

        );
    }
    public static void sendChargement(String  route_id, String date,String products_list,String image) throws Exception{

        SharedPreferences sp=mainActivity.getSharedPreferences("Login", mainActivity.MODE_PRIVATE);
        String sid = sp.getString(DataManager.SESSIONID,"");
        String user = sp.getString(DataManager.USERNAME,"");
        RequestBody body= new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("route_id",route_id)
                .addFormDataPart("date",date)
                .addFormDataPart("products_list",products_list)
                .addFormDataPart("image",image)
                .build();
        Log.v("URL_CHARGEMENT",URL_CHARGEMENT);
        Request request = new Request.Builder()
                .url(URL_CHARGEMENT).addHeader( "Content-Type", "multipart/form-data")
                .addHeader("Authorization", "Bearer "+sid)
                .addHeader("Cookie", "full_name=API;"+sid+"; system_user=yes; user_id=a"+user+"; user_image=")
                .post(body)
                .build();


        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        mainActivity.startToast("Erreur de connection ");
                        Log.v("error", e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.v("",""+response.body().string()+" "+(response.code()==200));
                        if(response.code()==200){
                            DataManager.image_path="";
                            mainActivity.startToast( "Facture ajouté avec succes" );
                            Log.v("Success", "Success\"Success\"\"Success\"\"Success\"\"Success\"");
                            mainActivity.clearData();
                        }else{
                            mainActivity.startToast( "Erreur lors de l'envoi de la facture !"+response.code() );

                        }

                    }
                }

        );
        /*
        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        mainActivity.startToast( "Erreur de connection ... ");
                        mainActivity.setAddScanIsEnabled(true);
                        mainActivity.clearData();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.v("response ",response.toString());
                        if(response.code()==200){
                            DataManager.image_path="";
                            mainActivity.startToast( "Facture ajouté avec succes" );
                            mainActivity.clearData();
                        }else{
                            mainActivity.startToast( "Erreur lors de l'envoi de la facture !" );

                        }

                    }
                }

        );*/


    }
    public static void getChargements () throws Exception {

        SharedPreferences sp=mainActivity.getSharedPreferences("Login", mainActivity.MODE_PRIVATE);
        String sid = sp.getString(DataManager.SESSIONID,"");
        String user = sp.getString(DataManager.USERNAME,"");
        String routes = sp.getString(DataManager.ROUTE,"");
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        String url=URL_LISTCHARGEMENTS;
        if(routes!=""){
                url=url+"&filters=[[%22route_id%22,%22in%22,%22"+routes+"%22]]";
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer "+sid)
                .addHeader("Cookie", "full_name=API;"+sid+"; system_user=yes; user_id="+user+"; user_image=")
                .build();
        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        mainActivity.startToast("Erreur de connection ... ");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try{
                            String s = response.body().string();
                            Log.v("response",s);
                            JSONObject json = new JSONObject(s);
                            JSONArray data = new JSONArray(json.getString("data"));
                            Log.v("data",data.toString());
                            ArrayList<Chargement> list = new ArrayList<Chargement>();
                            for(int i=0; i <data.length() ;i++){
                                JSONObject jInvoice = new JSONObject(data.get(i).toString());
                                String route = jInvoice.getString("route_id") ;
                                String date= jInvoice.getString("date") ;
                                String produits = jInvoice.getString("products_list") ;
                                String image = jInvoice.getString("image") ;
                                list.add(new Chargement(date,route,produits,image));
                            }
                            mainActivity.chargementsList.setListRequest(list);
                        }catch (Exception e){
                            mainActivity.startToast("Erreur du serveur ... ");


                        }
                    }
                }

        );

    }


    public static void cancelLoading(){
        if(cameraScanActivity == null){
            return ;

        }
        if(cameraScanActivity.getType()== CameraScanActivity.SEND_CHARGEMENT){
            mainActivity.setChargementIsEnabled(true);
        }else{
            mainActivity.setAddScanIsEnabled(true);
        }
    }

    public static void setActivateLogin(boolean b) {
        loginActivity.setLoginIsEnabled(b);
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void runURL(String url) {
        String[] splitUrl= url.split("/");
        String pdfUrl = PDF_API+ splitUrl[splitUrl.length - 1];
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
        mainActivity.startActivity(browserIntent);

    }
    public static void setImageText(String type, String status) {
        mainActivity.setImageText(type,status);
    }

    public static void getAccess(String user,String sid) throws Exception{
        MediaType mediaType = MediaType.parse("text/plain");

        magasins="";
        routes="";
        Request request = new Request.Builder()
                .url(URL_ACCESS+user)
                .get()
                .addHeader("Authorization", "Bearer "+sid)
                .addHeader("Cookie", "full_name=API;"+sid+"; system_user=yes; user_id="+user+"; user_image=")
                .build();
        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        mainActivity.startToast("Erreur de connection ... ");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try{
                            String s = response.body().string();
                            JSONObject json = new JSONObject(s);
                            JSONObject access= new JSONObject(json.getString("data"));
                            JSONArray routesList= new JSONArray(access.getString("routes_list"));
                            for(int i=0; i <routesList.length() ;i++) {
                                JSONObject jRoute= new JSONObject(routesList.get(i).toString());
                                getMagasins(jRoute.getString("route"),user,sid);
                                routes = routes +jRoute.getString("route")+",";
                                Log.v("AccessResponse route",routes);
                            }
                            if(routes==""){
                                mainActivity.startToast("AccessError : Verify ERP ACCESSS");
                            }else{

                                SharedPreferences sp=getMainActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor Ed=sp.edit();
                                Ed.putString(DataManager.ROUTE,routes);
                                Ed.apply();
                            }
                        }catch (Exception e ){
                            mainActivity.startToast("AccessError : Verify ERP ACCESSS");
                        }

                    }
                }

        );


    }

    private static void getMagasins(String route,String user, String sid) {
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url(URL_DistributionNRoute+route)
                .get()
                .addHeader("Authorization", "Bearer "+sid)
                .addHeader("Cookie", "full_name=API;"+sid+"; system_user=yes; user_id="+user+"; user_image=")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainActivity.startToast("Erreur de connection ... ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try{
                SharedPreferences sp=getMainActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();

                String s = response.body().string();
                JSONObject json = new JSONObject(s);
                JSONObject jdata= new JSONObject(json.getString("data"));
                JSONArray jMagasinArray=new JSONArray(jdata.getString("magasins"));
                for(int i=0; i <jMagasinArray.length() ;i++) {
                    //num_magasin
                    JSONObject jMagasin= new JSONObject(jMagasinArray.get(i).toString());
                    String numMagasin= jMagasin.getString("num_magasin") ;
                    magasins=magasins+numMagasin+",";
                    Ed.putString(DataManager.MAGASINS,magasins);
                    Ed.apply();



                }
                Log.v("MagList:",magasins);


                }
                catch (Exception e){
                    mainActivity.startToast("AccessError : Verify ERP ACCESSS");
                }
            }
        });

    }
}
