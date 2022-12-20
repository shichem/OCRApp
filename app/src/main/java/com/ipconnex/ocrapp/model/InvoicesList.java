package com.ipconnex.ocrapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipconnex.ocrapp.R;
import com.ipconnex.ocrapp.model.Invoice;

import java.util.ArrayList;

public class InvoicesList extends BaseAdapter {
    private final Context context ;
    private ArrayList<Invoice> invoicesArray;

    public InvoicesList(Context context) {
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

        private final TextView magasin;
        private final TextView client;

        private final TextView vendu;
        private final TextView retour;
        private final TextView total;
        public HolderDetailedView(View view){
            img = view.findViewById(R.id.invoiceImage);
            facture =  view.findViewById(R.id.invoiceText);
            showOption =  view.findViewById(R.id.show_option);
            magasin=  view.findViewById(R.id.magasinText);
            client=  view.findViewById(R.id.clientText);

            vendu=  view.findViewById(R.id.tVenduText);
            retour=  view.findViewById(R.id.tRetourlientText);
            total=  view.findViewById(R.id.totalText);
        }
    }
}
