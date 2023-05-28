package com.ipconnex.ocrapp.design;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ipconnex.ocrapp.CameraScanActivity;
import com.ipconnex.ocrapp.DataManager;
import com.ipconnex.ocrapp.R;
import com.websitebeaver.documentscanner.DocumentScanner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddScan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddScan extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String image = "";
    private TextInputLayout input_facture;
    private TextInputLayout input_client;
    private TextInputLayout input_magasin;
    private TextInputLayout input_qte;
    private TextInputLayout input_t_vendu;
    private TextInputLayout input_t_retour;
    private TextInputLayout input_total;
    private Intent intent;
    private TextInputLayout imageStatus;
    private View buttonSendInv ;
    private View buttonStartCam;
    private String[] options_days;
    private String[] options_month;
    private String[] options_years;
    private int selected_day=0,selected_month=0,selected_year=0;
    private AutoCompleteTextView sYear,sMonth,sDay,itemMagasin;
    Date date = new Date();
    SimpleDateFormat year_formatter = new SimpleDateFormat("yyyy");
    private Integer thisYear = new Integer(year_formatter.format(date));
    public void setDatesLists(){
        date = new Date();
        thisYear = new Integer(year_formatter.format(date));
        options_years=new String[5] ;
        DecimalFormat formatter = new DecimalFormat("00");
        for ( int i=0; i<5;i++){
            options_years[i]=""+(thisYear-i);
        }
        options_month=new String[12] ;
        for ( int i=0; i<12;i++){
            options_month[i]=formatter.format(i+1);
        }
        if(selected_month==3 || selected_month==5|| selected_month==8|| selected_month==10){
            // months with 30 days
            options_days=new String[30] ;
            for ( int i=0; i<30;i++){
                options_days[i]=formatter.format(i+1);
            }

        }else{
            if(selected_month==1){
                // february
                if((thisYear-selected_year)%4==0){ // 29 days
                    options_days=new String[29] ;
                    for ( int i=0; i<29;i++){
                        options_days[i]=formatter.format(i+1);
                    }

                }else{ //28 days
                    options_days=new String[28] ;
                    for ( int i=0; i<28;i++){
                        options_days[i]=formatter.format(i+1);
                    }

                }
            }else{ // months with 31 days
                options_days=new String[31] ;
                for ( int i=0; i<31;i++){
                    options_days[i]=formatter.format(i+1);
                }


            }

        }


        ArrayAdapter<String> adapterD = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_days);
        sDay.setAdapter(adapterD);
        sDay.setThreshold(adapterD.getCount());
        if(selected_day>=options_days.length){
            selected_day=0;
        }
        sDay.setText(sDay.getAdapter().getItem(selected_day).toString(), false);
        ArrayAdapter<String> adapterM = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_month);
        sMonth.setAdapter(adapterM);
        sMonth.setThreshold(adapterM.getCount());

        sMonth.setText(sMonth.getAdapter().getItem(selected_month).toString(), false);
        ArrayAdapter<String>  adapterY = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_years);
        sYear.setAdapter(adapterY);
        sYear.setThreshold(adapterY.getCount());

        sYear.setText(sYear.getAdapter().getItem(selected_year).toString(), false);

    }
    public void setDaysList(){
        DecimalFormat formatter = new DecimalFormat("00");
        if(selected_month==3 || selected_month==5|| selected_month==8|| selected_month==10){
            // months with 30 days
            options_days=new String[30] ;
            for ( int i=0; i<30;i++){
                options_days[i]=formatter.format(i+1);
            }

        }else{
            if(selected_month==1){
                // february
                if((thisYear-selected_year)%4==0){ // 29 days
                    options_days=new String[29] ;
                    for ( int i=0; i<29;i++){
                        options_days[i]=formatter.format(i+1);
                    }

                }else{ //28 days
                    options_days=new String[28] ;
                    for ( int i=0; i<28;i++){
                        options_days[i]=formatter.format(i+1);
                    }

                }
            }else{ // months with 31 days
                options_days=new String[31] ;
                for ( int i=0; i<31;i++){
                    options_days[i]=formatter.format(i+1);
                }


            }

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_days);
        sDay.setAdapter(adapter);
        sDay.setThreshold(adapter.getCount());
        if(selected_day>=options_days.length){
            selected_day=0;
        }
        sDay.setText(sDay.getAdapter().getItem(selected_day).toString(), false);
    }



    public void setData(String img , String facture ,String magasin ,String client,String t_vendu ,String t_retour ,String total, String qte ,String date ){

        this.image = img ;
        Log.v(magasin ,img+" "+facture);
        this.input_facture.getEditText().setText(facture);
        this.input_client.getEditText().setText(client);
        this.input_magasin.getEditText().setText(magasin);
        this.input_qte.getEditText().setText(qte);
        this.input_t_vendu.getEditText().setText(t_vendu);
        this.input_t_retour.getEditText().setText(t_retour);
        this.input_total.getEditText().setText(total);
        if(date.split("/").length==3){

            try{
                int a= Integer.parseInt(date.split("/")[1])-1;
                sMonth.setText(sMonth.getAdapter().getItem(a).toString(), false);
                selected_month=a;
            }catch(Exception e){

            }
            try{
                int a=thisYear-Integer.parseInt(date.split("/")[2]);
                sYear.setText(sYear.getAdapter().getItem(a).toString(), false);
                selected_year=a;
            }catch(Exception e){

            }
            try{
                int a= Integer.parseInt(date.split("/")[0])-1;
                sDay.setText(sDay.getAdapter().getItem(a).toString(), false);
                selected_day =a;
            }catch(Exception e){

            }
        }

        setIsEnabled(true);
    }

    public ArrayList<String> getData(){

        ArrayList<String> result = new ArrayList<String>();
        result.add(image);
        result.add(input_facture.getEditText().getText().toString());
        result.add(input_magasin.getEditText().getText().toString());
        result.add(input_client.getEditText().getText().toString());
        result.add(input_qte.getEditText().getText().toString());
        result.add(input_t_vendu.getEditText().getText().toString());
        result.add(input_t_retour.getEditText().getText().toString());
        result.add(input_total.getEditText().getText().toString());

        return result;
    }
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public AddScan() {


    }

    // TODO: Rename and change types and number of parameters
    public static AddScan newInstance(String param1, String param2) {
        AddScan fragment = new AddScan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // start document scan
        View inflate = inflater.inflate(R.layout.fragment_add_scan, container, false);
        buttonSendInv = inflate.findViewById(R.id.validateInvoice);
        buttonStartCam = inflate.findViewById(R.id.startCamera);
        input_facture= (TextInputLayout) inflate.findViewById(R.id.factureField);
        input_client= (TextInputLayout)  inflate.findViewById(R.id.clientField);
        input_magasin= (TextInputLayout)  inflate.findViewById(R.id.magasinField);
        input_t_vendu= (TextInputLayout)  inflate.findViewById(R.id.venduField);
        input_t_retour= (TextInputLayout)  inflate.findViewById(R.id.retourField);
        input_total= (TextInputLayout)  inflate.findViewById(R.id.totalField);
        input_qte=(TextInputLayout) inflate.findViewById(R.id.qteField);
        imageStatus=(TextInputLayout) inflate.findViewById(R.id.imageStatus);
        intent= new Intent(getActivity(), CameraScanActivity.class);
        itemMagasin = inflate.findViewById(R.id.magasinItem);
        ArrayList <String>magasins = new ArrayList<>();

        SharedPreferences sp= getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String m= sp.getString(DataManager.MAGASINS,"");
        Log.v("Magasins",m);
        for (String s : m.split(",")) {
            Log.v("MagasinsS",s);
            if(s.length()>0)
                magasins.add(s);
        }
        ArrayAdapter<String> adapterRoutes = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,magasins);
        itemMagasin.setAdapter(adapterRoutes);
        sDay=inflate.findViewById(R.id.dropBoxDay);
        sMonth=inflate.findViewById(R.id.dropBoxMonth);
        sYear=inflate.findViewById(R.id.dropBoxYear);
        setDatesLists();
        sDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                     Log.v("Day ",""+i );
                     selected_day=i;
                 }
             }

        );

        sDay.setText(sDay.getAdapter().getItem(selected_day).toString(), false);
        sMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                          @Override
                                          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                              Log.v("month ",""+i );
                                              selected_month=i;
                                              setDaysList();
                                          }
                                      }

        );
        sMonth.setText(sMonth.getAdapter().getItem(selected_month).toString(), false);




        sYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            Log.v("Year ",""+i );
                                            selected_year=i;
                                            setDaysList();
                                        }
                                    }

        );
        sYear.setText(sYear.getAdapter().getItem(selected_year).toString(), false);

        // TODO set Editable false
        setIsEnabled(true);
        buttonSendInv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   Log.v("Sending","data");
                ArrayList<String> data = getData();

                String image= data.get(0);
                String facture= data.get(1);
                String magasin= data.get(2);
                String  s_day=""+(selected_day+1);
                if(selected_day+1 < 10){
                    s_day="0"+(selected_day+1);
                }
                String  s_month=""+(selected_month+1);
                if(selected_day+1 < 10){
                    s_month="0"+(selected_day+1);
                }
                String date_facture=""+(thisYear-selected_year)+"-"+s_month+"-"+s_day;
                Log.v("date facturation",date_facture);
                String qte=data.get(3);
                String client= data.get(4);
                String t_retour= data.get(5);
                String t_vendu= data.get(6);
                String total= data.get(7);

                try{
                    setIsEnabled(false);
                    DataManager.sendForm(image,facture,magasin,client,date_facture,qte,t_vendu,t_retour,total);
                }catch (Exception e ){

                }
            }
        });
        buttonStartCam.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraScanActivity.setType(CameraScanActivity.SEND_FACTURE);
                setIsEnabled(false);
                startActivity(intent);
            }
        });
        return inflate;
    }
    public void setIsEnabled(boolean b ){
        input_facture.setEnabled(b);
        input_client.setEnabled(b);
        input_magasin.setEnabled(b);
        input_qte.setEnabled(b);
        input_t_vendu.setEnabled(b);
        input_t_retour.setEnabled(b);
        input_total.setEnabled(b);
        buttonStartCam.setEnabled(b);
        buttonSendInv.setEnabled(b);
        sDay.setEnabled(b);
        sMonth.setEnabled(b);
        sYear.setEnabled(b);

    }
    public void setImageStatus(String status) {
        imageStatus.setHint(status);
    }
}