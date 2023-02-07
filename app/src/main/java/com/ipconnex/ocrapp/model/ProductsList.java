package com.ipconnex.ocrapp.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.ipconnex.ocrapp.MainActivity;
import com.ipconnex.ocrapp.R;

import java.util.ArrayList;

public class ProductsList extends BaseAdapter {
    private ArrayList<String> productsArray;

    public ProductsList( ArrayList<String> productsArray) {
        this.productsArray = productsArray;
        Log.v("ziiiiiiiiiiiii",""+productsArray.size());
    }

    public void setProductsArray(ArrayList<String> productsArray) {
        this.productsArray = productsArray;notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return productsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return productsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String produit= productsArray.get(i);
        Context context= viewGroup.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.display_product,viewGroup,false) ;
        HolderView holderView=new HolderView(view);
        view.setTag(holderView);
        holderView.produit.setText(produit);
        Log.v("i",produit+" "+i);
        return view;
    }
    private static class HolderView{
        private final TextView produit;

        public HolderView(View view){
            produit=view.findViewById(R.id.id_product);

        }
    }
}
