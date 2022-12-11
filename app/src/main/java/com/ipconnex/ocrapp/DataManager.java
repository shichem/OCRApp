package com.ipconnex.ocrapp;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

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

    private static  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE = MediaType.parse("image");
    private static final String URL_FACTURES = "https://ocr-api.ipconnex.com/api/factures/";
    private static final String URL_CAPTURES= "https://ocr-api.ipconnex.com/api/captures/";
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
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.v("Api Response",response.body().string());
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
                        Log.v("Errpr api ",e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.v("done",response.body().string());

                    }
                }

        );

    }


}
