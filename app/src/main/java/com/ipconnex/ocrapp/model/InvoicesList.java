package com.ipconnex.ocrapp.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ipconnex.ocrapp.MainActivity;
import com.ipconnex.ocrapp.R;
import com.ipconnex.ocrapp.design.ScansList;
import com.ipconnex.ocrapp.model.Invoice;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InvoicesList extends BaseAdapter {
    private final MainActivity context ;
    private ArrayList<Invoice> invoicesArray;

    public InvoicesList(MainActivity context ){
        this.context = context;
        this.invoicesArray =new ArrayList<Invoice>();
    }

    public void setInvoicesArray(ArrayList<Invoice> invoicesArray) {

        this.invoicesArray = invoicesArray;
        this.updateResults();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return invoicesArray.size();
    }

    @Override
    public Invoice getItem(int i) {
        return invoicesArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Invoice inv = invoicesArray.get(i);

        if(inv.isDetailed()){
            view = LayoutInflater.from(context).inflate(R.layout.display_invoice_detailed,viewGroup,false) ;
            HolderDetailedView holderView=new HolderDetailedView(view);
            view.setTag(holderView);
            if( !inv.getCaptureImgUrl().startsWith("http")){
                holderView.downloadPDFButton.setEnabled(false);
            }
            holderView.showOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inv.setDetailed();
                    updateResults();
                    context.getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, context.scansList).commit();

                }
            }); // hide set invoice isDetailed
            holderView.downloadPDFButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String fileName="image";
                    String image_url=inv.getCaptureImgUrl().replace("http://"," https://");

                    OkHttpClient client = new OkHttpClient();
                    holderView.downloadPDFButton.setEnabled(false);
                    Request request = new Request.Builder()
                            .url(image_url)
                            .build();
                    client.newCall(request).enqueue(
                            new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                                    context.startToast("Error while downloading the file");
                                }
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {

                                    try {
                                        InputStream inputStream = response.body().byteStream();
                                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                        Image image ;
                                        Document document = new Document();
                                        String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();
                                        PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/OCRApp"+inv.getFactureNum()+".pdf")); //  Change pdf's name.
                                        Log.v("path",directoryPath);
                                        context.startToast("Le fichier à été créé avec succes");
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                document.open();

                                            }
                                        });

                                        image = Image.getInstance(new URL(image_url)); // Change image's name and extension.

                                        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                                                - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                                        float scaler_h = ((document.getPageSize().getHeight() - document.topMargin()
                                                - document.bottomMargin() - 0) / image.getHeight()) * 100; // 0 means you have no indentation. If you have any, change it.
                                        if(scaler_h<scaler){
                                            scaler=scaler_h;
                                        }
                                        image.scalePercent(scaler);
                                        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

                                        document.add(image);
                                        document.close();
                                        File file = new File(directoryPath + "/OCRApp"+inv.getFactureNum()+".pdf");
                                        Intent target = new Intent(Intent.ACTION_VIEW);
                                        target.setDataAndType(Uri.fromFile(file),"application/pdf");
                                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                                        Intent intent = Intent.createChooser(target, "Open File");
                                        try {
                                            context.startActivity(intent);
                                        } catch (Exception e) {
                                            context.startToast("Error while opening the file");
                                            // Instruct the user to install a PDF reader here, or something
                                        }
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                holderView.downloadPDFButton.setEnabled(true);
                                            }
                                        });
                                        Log.v("Response",""+bitmap.getByteCount());
                                    }catch (Exception e){
                                        Log.v("Error",e.getMessage());
                                        context.startToast("Error :"+e.getMessage());
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                holderView.downloadPDFButton.setEnabled(true);
                                            }
                                        });
                                    }
                                }
                            });


                }
            }); // download image as pdf of this invoice
            holderView.facture.setText(inv.getFacture());
            holderView.magasin.setText(inv.getMagasin());
            Log.v("mag/client",inv.getMagasin()+" "+inv.getClient());
            holderView.client.setText(inv.getClient());
            holderView.vendu.setText(inv.getT_vendu());
            holderView.retour.setText(inv.getT_retour());
            holderView.total.setText(inv.getTotal());

        }else{
            view = LayoutInflater.from(context).inflate(R.layout.display_invoice,viewGroup,false) ;
            HolderView holderView=new HolderView(view);
            Log.v("Hide details",inv.getFacture());
            view.setTag(holderView);
            holderView.facture.setText(inv.getFacture());
            holderView.prix.setText(inv.getTotal());
        }

        return view;

    }
    public void updateResults() {
        notifyDataSetChanged();


    }

    private static class HolderView{
        private final ImageView img;
        private final TextView facture;
        private final ImageView showOption ;
        private final TextView prix ;

        public HolderView(View view){
            img = view.findViewById(R.id.invoiceImage);
            facture =  view.findViewById(R.id.invoiceText);
            showOption =  view.findViewById(R.id.show_option);
            prix = view.findViewById(R.id.totalText);

        }
    }
    private static class HolderDetailedView{
        private final ImageView img;
        private final TextView facture;
        private final ImageView showOption ;
        private final Button downloadPDFButton ;

        private final TextView magasin;
        private final TextView client;

        private final TextView vendu;
        private final TextView retour;
        private final TextView total;
        public HolderDetailedView(View view){
            img = view.findViewById(R.id.invoiceImage);
            facture =  view.findViewById(R.id.invoiceText);
            showOption =  view.findViewById(R.id.show_option);
            downloadPDFButton=view.findViewById(R.id.bttnDownloadPDF);

            magasin=  view.findViewById(R.id.magasinText);
            client=  view.findViewById(R.id.clientText);

            vendu=  view.findViewById(R.id.tVenduText);
            retour=  view.findViewById(R.id.tRetourlientText);
            total=  view.findViewById(R.id.totalText);
        }
    }
}
