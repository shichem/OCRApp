package com.ipconnex.ocrapp.design;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputLayout;
import com.ipconnex.ocrapp.CameraScanActivity;
import com.ipconnex.ocrapp.DataManager;
import com.ipconnex.ocrapp.MainActivity;
import com.ipconnex.ocrapp.R;
import com.ipconnex.ocrapp.model.ProductsList;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddChargement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddChargement extends Fragment {
    private ArrayList<String> data;
    private String image ="";
    private ProductsList productsList;
    private String[] options_days;
    private String[] options_month;
    private String[] options_years;
    private int selected_day=0,selected_month=0,selected_year=0;
    private AutoCompleteTextView sYear,sMonth,sDay,itemInput;
    Date date = new Date();
    SimpleDateFormat year_formatter = new SimpleDateFormat("yyyy");
    private Integer thisYear = new Integer(year_formatter.format(date));
    private Button supprimer,ajouter,valider;
    private Button startCamera;
    private TextInputLayout routeInput;

    private Intent intent;
    private boolean listContains(String s){
        for(int i =0;i<data.size();i++){
            if(s.compareTo(data.get(i))==0) return true ;

        }
        return false;
    }

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


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_days);
        sDay.setAdapter(adapter);
        if(selected_day>=options_days.length){
            selected_day=0;
        }
        sDay.setText(sDay.getAdapter().getItem(selected_day).toString(), false);
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_month);
        sMonth.setAdapter(adapter);

        sMonth.setText(sMonth.getAdapter().getItem(selected_month).toString(), false);
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_years);
        sYear.setAdapter(adapter);

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
        if(selected_day>=options_days.length){
            selected_day=0;
        }
        sDay.setText(sDay.getAdapter().getItem(selected_day).toString(), false);
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddChargement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddChargement.
     */
    // TODO: Rename and change types and number of parameters
    public static AddChargement newInstance(String param1, String param2) {
        AddChargement fragment = new AddChargement();
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
        View f = inflater.inflate(R.layout.fragment_add_chargement, container, false);
        ajouter = f.findViewById(R.id.ajouter);
        supprimer= f.findViewById(R.id.supprimer);
        valider=f.findViewById(R.id.validateChargement);
        startCamera=f.findViewById(R.id.startCamera);
        routeInput=f.findViewById(R.id.routeField);
        itemInput=f.findViewById(R.id.dropBox);
        AutoCompleteTextView dropList = f.findViewById(R.id.dropBox);
        intent= new Intent(getActivity(), CameraScanActivity.class);
        data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,data);
        dropList.setAdapter(adapter);
        ListView listView = (ListView) f.findViewById(R.id.productList);
        productsList=new ProductsList(data);
        listView.setAdapter(productsList);
        productsList = new ProductsList(data);
        listView.setAdapter(productsList);
        if(data.size() >0)
        {
            dropList.setText("", false);
        }
        String s=dropList.getText().toString();
        if( s.length()!= 0){
            Log.v("Text changed",s.toString());
            if(listContains(s)){
                supprimer.setEnabled(true);ajouter.setEnabled(false);
            }else{
                supprimer.setEnabled(false);ajouter.setEnabled(true);
            }
        }else{
            supprimer.setEnabled(false);ajouter.setEnabled(false);

        }
        dropList.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    Log.v("Text changed",s.toString()+" "+(listContains(s.toString())));
                    if(listContains(s.toString())){
                        supprimer.setEnabled(true);ajouter.setEnabled(false);
                    }else{
                        supprimer.setEnabled(false);ajouter.setEnabled(true);
                    }
                    // if data contains s  bttn
                    // else                bttn supprimer.enable(false);ajouter.enable(true);
                }else{
                    supprimer.setEnabled(false);ajouter.setEnabled(false);

                }

            }
        });
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!itemInput.isEnabled()){
                    return;
                }
                String s = dropList.getText().toString();
                data.remove(s);
                dropList.setText("", false);
                ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.drop_down_items,data);
                dropList.setAdapter(adapter);
                productsList.setProductsArray(data);
            }
        });
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to data
                if(!itemInput.isEnabled()){
                    return;
                }
                String s = dropList.getText().toString();
                data.add(s);

                ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.drop_down_items,data);
                dropList.setAdapter(adapter);
                dropList.setText("", false);
                productsList.setProductsArray(data);
            }
        });


        sDay=f.findViewById(R.id.dropBoxDay);
        sMonth=f.findViewById(R.id.dropBoxMonth);
        sYear=f.findViewById(R.id.dropBoxYear);
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
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// TODO send to erp ! "route_id","date","products_list","image"
                Log.v("Sending","Rapport de Chargement");

                String route= routeInput.getEditText().getText().toString();
                String date=""+(selected_day+1)+"/"+(selected_month+1)+"/"+(thisYear-selected_year);
                String products="";
                for(String s:data){
                    products=products+s+" ";
                }
                try{
                    setIsEnabled(false);
                    DataManager.sendChargement(route,date,products,image);
                }catch (Exception e ){

                }

            }
        });
        startCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraScanActivity.setType(CameraScanActivity.SEND_CHARGEMENT);
                setIsEnabled(false);
                startActivity(intent);

            }
        });

        return f;
    }

    public void setIsEnabled(boolean b) {
        sYear.setEnabled(b);
        sMonth.setEnabled(b);
        sDay.setEnabled(b);
        routeInput.setEnabled(b);
        valider.setEnabled(b);
        itemInput.setEnabled(b);

    }

    public void setData(String image, String route_id, String date, String products_list) {
        this.image=image;
        routeInput.getEditText().setText(route_id);
        data=new ArrayList<>();
        for ( String s: products_list.split(" ")){
            data.add(s);
        }
        productsList.setProductsArray(data);
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


    }
}