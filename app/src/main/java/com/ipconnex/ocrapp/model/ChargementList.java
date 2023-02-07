package com.ipconnex.ocrapp.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ipconnex.ocrapp.DataManager;
import com.ipconnex.ocrapp.MainActivity;
import com.ipconnex.ocrapp.R;

import java.util.ArrayList;

public class ChargementList extends BaseAdapter {
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    private final MainActivity context ;
    private ArrayList<Chargement> chargementsArray;

    private String filterDateA="",filterDateB="",filterRoute="";

    public ChargementList(MainActivity context) {
        this.context = context;
        this.chargementsArray = new ArrayList<Chargement>();
    }

    public void setChargementsArray(ArrayList<Chargement> chargementsArray) {
        this.chargementsArray = chargementsArray;
        Log.v("len",""+chargementsArray.size());
        notifyDataSetChanged();
    }

    public void setFilters(String filterDateA,String filterDateB,String filterRoute) {
        this.filterDateA = filterDateA;
        this.filterDateB = filterDateB;
        this.filterRoute = filterRoute;
        notifyDataSetChanged();

    }

    public String getFilterRoute() {
        return filterRoute;
    }

    @Override
    public int getCount() {
        return chargementsArray.size();
    }

    @Override
    public Object getItem(int i) {
        return chargementsArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Chargement chargement= chargementsArray.get(i);
        Context context= viewGroup.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.display_chargement,viewGroup,false) ;
        HolderView holderView=new HolderView(view);
        view.setTag(holderView);
        holderView.produits.setText(chargement.getProduits());
        holderView.route.setText(chargement.getRoute());
        holderView.date.setText(chargement.getDate());
        if(!chargement.verify(filterDateA,filterDateB,filterRoute)){
            return new View(view.getContext());
        }
        if( !chargement.getImage().startsWith("http")){
            holderView.downloadPDFButton.setEnabled(false);
        }
        holderView.downloadPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image_url= chargement.getImage();
                DataManager.runURL(image_url);

            }
        });
        return view;
    }
    private static class HolderView{
        private final TextView produits;
        private final TextView route;
        private final TextView date;

        private final Button downloadPDFButton;

        public HolderView(View view){
            produits=view.findViewById(R.id.produitsText);
            route=view.findViewById(R.id.routeText);
            date=view.findViewById(R.id.dateText);
            downloadPDFButton=view.findViewById(R.id.bttnRapportPDF);

        }
    }
}
