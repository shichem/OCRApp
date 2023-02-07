package com.ipconnex.ocrapp.model;

import android.util.Log;

public class Chargement {
    private String date;
    private String route;
    private String produits;
    private String image_link;

    public Chargement(String date, String route, String produits,String image_link) {
        this.date = date;
        this.route = route;
        this.produits = produits;
        this.image_link=image_link;
    }

    public String getDate() {
        return "Date: "+date;
    }

    public String getRoute() {
        return "Route: "+route;
    }

    public String getProduits() {
        return "Produits: "+produits;
    }

    public String getImage() {
        return image_link;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setProduits(String produits) {
        this.produits = produits;
    }

    public void setImage(String image_link) {this.image_link=image_link;}
    public boolean verify(String date_before,String date_after,String route ){
        Log.v("dates :",date_before+"is less than "+this.date +" result "+CompareAttributes.compareDates(date_before,this.date));

        Log.v("dates :",this.date+" is less than "+date_after+" result "+CompareAttributes.compareDates(this.date,date_after));
        return this.route.contains(route) && CompareAttributes.compareDates(date_before,this.date) && CompareAttributes.compareDates(this.date,date_after) ;
    }


}
