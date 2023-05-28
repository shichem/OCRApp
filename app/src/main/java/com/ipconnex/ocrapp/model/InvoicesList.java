package com.ipconnex.ocrapp.model;

import android.app.Activity;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.ipconnex.ocrapp.DataManager;
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
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    private final MainActivity context ;
    private ArrayList<Invoice> invoicesArray;
    private String filterDateA="",filterDateB="",filterFacture="",filterMagasin="",filterClient="";
    public InvoicesList(MainActivity context ){
        this.context = context;
        this.invoicesArray =new ArrayList<Invoice>();
    }

    public void setInvoicesArray(ArrayList<Invoice> invoicesArray) {

        this.invoicesArray = invoicesArray;
        this.updateResults();
        this.notifyDataSetChanged();
    }


    public void setFilters(String filterDateA,String filterDateB,String filterFacture,String filterMagasin,String filterClient) {
        this.filterDateA = filterDateA;
        this.filterDateB = filterDateB;
        this.filterFacture= filterFacture;
        this.filterMagasin= filterMagasin;
        this.filterClient=filterClient;
        notifyDataSetChanged();

    }

    public String getFilterFacture() {
        return filterFacture;
    }

    public String getFilterMagasin() {
        return filterMagasin;
    }

    public String getFilterClient() {
        return filterClient;
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
        Log.v("Verif",""+inv.verify(filterDateA,filterDateB,filterFacture,filterMagasin,filterClient));
        if(!inv.verify(filterDateA,filterDateB,filterFacture,filterMagasin,filterClient)){
            return new View(viewGroup.getContext());
        }
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

                    String image_url=inv.getCaptureImgUrl(); //.replace("http://"," https://")
                    DataManager.runURL(image_url);

                    /*
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
                                        //View sheetView = DataManager.getMainActivity().inflate(R.layout.bottom_sheet_rapport, null);

                                        Image image ;
                                        Document document = new Document();
                                        File path = context.getExternalCacheDir();
                                        File pdfFile=new File(path,"OCRApp.pdf");
                                        FileOutputStream fileOutputStream= new FileOutputStream(pdfFile);
                                        PdfWriter.getInstance(document, fileOutputStream); //  Change pdf's name.
                                        Log.v("path",pdfFile.toString()+ "  :" +pdfFile.toString().length());
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
                                        Uri pdfURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);
                                        Log.v("Uri",pdfURI.getPath()+"   "+pdfFile.toURI());
                                        Intent target = new Intent(Intent.ACTION_VIEW);
                                        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                        target.setDataAndType(pdfURI,"application/pdf");
                                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                                        Intent intent = Intent.createChooser(target, "Open File");
                                        try {
                                            context.startActivity(intent);
                                        } catch (Exception e) {
                                            context.startToast("Error while opening the file "+e.getMessage());
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
                                        try {
                                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    READ_STORAGE_PERMISSION_REQUEST_CODE);
                                        } catch (Exception exp) {
                                            e.printStackTrace();

                                        }
                                    }
                                }
                            });*/


                }
            }); // download image as pdf of this invoice
            holderView.facture.setText(inv.getFacture());
            holderView.magasin.setText(inv.getMagasin());
            holderView.client.setText(inv.getClient());
            holderView.vendu.setText(inv.getT_vendu());
            holderView.retour.setText(inv.getT_retour());
            holderView.total.setText(inv.getTotal());

        }else{
            view = LayoutInflater.from(context).inflate(R.layout.display_invoice,viewGroup,false) ;
            HolderView holderView=new HolderView(view);
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
