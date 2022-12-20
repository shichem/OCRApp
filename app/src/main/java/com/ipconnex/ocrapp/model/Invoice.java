package com.ipconnex.ocrapp.model;


public class Invoice {
    private String captureImgUrl;
    private String facture;
    private String magasin;
    private String client;
    private String t_vendu;
    private String t_retour;
    private String total;
    private boolean isDetailed=false;

    public Invoice(String captureImgUrl, String facture, String magasin, String client, String t_vendu, String t_retour, String total) {
        this.captureImgUrl = captureImgUrl;
        this.facture = facture;
        this.magasin = magasin;
        this.client = client;
        this.t_vendu = t_vendu;
        this.t_retour = t_retour;
        this.total = total;
    }

    public void setCaptureImgUrl(String captureImgUrl) {
        this.captureImgUrl = captureImgUrl;
    }

    public void setFacture(String facture) {
        this.facture = facture;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setT_vendu(String t_vendu) {
        this.t_vendu = t_vendu;
    }

    public void setT_retour(String t_retour) {
        this.t_retour = t_retour;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setDetailed() {
        isDetailed = !isDetailed;
    }

    public String getCaptureImgUrl() {
        return captureImgUrl;
    }

    public String getFacture() {
        return "Facture : "+ facture;
    }

    public String getMagasin() {
        return "Magasin : "+magasin;
    }

    public String getClient() {
        return "Client : "+client;
    }

    public String getT_vendu() {
        return "Vendu : $"+t_vendu;
    }

    public String getT_retour() {
        return "Retour : $"+t_retour;
    }

    public String getTotal() {
        return "Total: $"+total;
    }

    public boolean isDetailed() {
        return isDetailed;
    }
}
