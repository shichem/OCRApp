package com.ipconnex.ocrapp.model;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class CompareAttributes {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
    public static boolean compareDates(String lower,String grater){
        if(lower.compareTo("")==0 || grater.compareTo("")==0 ) return true;


        try {
            Date date1 = sdf.parse(lower);
            Date date2 = sdf.parse(grater);
            if(date1.compareTo(date2) <=0){
                return true;
            }

        }catch (Exception e){ return false ; }
        return false;
    }
}
