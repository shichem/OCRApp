package com.ipconnex.ocrapp.design;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.ipconnex.ocrapp.DataManager;
import com.ipconnex.ocrapp.MainActivity;
import com.ipconnex.ocrapp.R;
import com.ipconnex.ocrapp.model.Chargement;
import com.ipconnex.ocrapp.model.ChargementList;
import com.ipconnex.ocrapp.model.Invoice;
import com.ipconnex.ocrapp.model.InvoicesList;
import com.ipconnex.ocrapp.model.ProductsList;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChargementsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChargementsList extends Fragment {

    private ArrayList<String> data;
    private ChargementList chargementList;
    private MainActivity mainActivity;

    private FloatingActionButton searchFAB ;
    private String[] options_daysA;
    private String[] options_monthA;
    private String[] options_yearsA;
    private int selected_dayA=30,selected_monthA=11,selected_yearA=0;
    private AutoCompleteTextView sYearA,sMonthA,sDayA;
    private String[] options_daysB;
    private String[] options_monthB;
    private String[] options_yearsB;
    private int selected_dayB=0,selected_monthB=0,selected_yearB=4;
    private AutoCompleteTextView sYearB,sMonthB,sDayB;

    private TextInputLayout routeField;

    Date date = new Date();
    SimpleDateFormat year_formatter = new SimpleDateFormat("yyyy");
    private Integer thisYear = new Integer(year_formatter.format(date));



    public void setDatesListsA(){
        date = new Date();
        thisYear = new Integer(year_formatter.format(date));
        options_yearsA=new String[5] ;
        DecimalFormat formatter = new DecimalFormat("00");
        for ( int i=0; i<5;i++){
            options_yearsA[i]=""+(thisYear-i);
        }
        options_monthA=new String[12] ;
        for ( int i=0; i<12;i++){
            options_monthA[i]=formatter.format(i+1);
        }
        if(selected_monthA==3 || selected_monthA==5|| selected_monthA==8|| selected_monthA==10){
            // months with 30 days
            options_daysA=new String[30] ;
            for ( int i=0; i<30;i++){
                options_daysA[i]=formatter.format(i+1);
            }

        }else{
            if(selected_monthA==1){
                // february
                if((thisYear-selected_yearA)%4==0){ // 29 days
                    options_daysA=new String[29] ;
                    for ( int i=0; i<29;i++){
                        options_daysA[i]=formatter.format(i+1);
                    }

                }else{ //28 days
                    options_daysA=new String[28] ;
                    for ( int i=0; i<28;i++){
                        options_daysA[i]=formatter.format(i+1);
                    }

                }
            }else{ // months with 31 days
                options_daysA=new String[31] ;
                for ( int i=0; i<31;i++){
                    options_daysA[i]=formatter.format(i+1);
                }


            }

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_daysA);
        sDayA.setAdapter(adapter);
        if(selected_dayA>=options_daysA.length){
            selected_dayA=0;
        }
        sDayA.setText(sDayA.getAdapter().getItem(selected_dayA).toString(), false);
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_monthA);
        sMonthA.setAdapter(adapter);

        sMonthA.setText(sMonthA.getAdapter().getItem(selected_monthA).toString(), false);
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_yearsA);
        sYearA.setAdapter(adapter);

        sYearA.setText(sYearA.getAdapter().getItem(selected_yearA).toString(), false);

    }
    public void setDaysListA(){
        DecimalFormat formatter = new DecimalFormat("00");
        if(selected_monthA==3 || selected_monthA==5|| selected_monthA==8|| selected_monthA==10){
            // months with 30 days
            options_daysA=new String[30] ;
            for ( int i=0; i<30;i++){
                options_daysA[i]=formatter.format(i+1);
            }

        }else{
            if(selected_monthA==1){
                // february
                if((thisYear-selected_yearA)%4==0){ // 29 days
                    options_daysA=new String[29] ;
                    for ( int i=0; i<29;i++){
                        options_daysA[i]=formatter.format(i+1);
                    }

                }else{ //28 days
                    options_daysA=new String[28] ;
                    for ( int i=0; i<28;i++){
                        options_daysA[i]=formatter.format(i+1);
                    }

                }
            }else{ // months with 31 days
                options_daysA=new String[31] ;
                for ( int i=0; i<31;i++){
                    options_daysA[i]=formatter.format(i+1);
                }


            }

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_daysA);
        sDayA.setAdapter(adapter);
        if(selected_dayA>=options_daysA.length){
            selected_dayA=0;
        }
        sDayA.setText(sDayA.getAdapter().getItem(selected_dayA).toString(), false);
    }

    public void setDatesListsB(){
        date = new Date();
        thisYear = new Integer(year_formatter.format(date));
        options_yearsB=new String[5] ;
        DecimalFormat formatter = new DecimalFormat("00");
        for ( int i=0; i<5;i++){
            options_yearsB[i]=""+(thisYear-i);
        }
        options_monthB=new String[12] ;
        for ( int i=0; i<12;i++){
            options_monthB[i]=formatter.format(i+1);
        }
        if(selected_monthB==3 || selected_monthB==5|| selected_monthB==8|| selected_monthB==10){
            // months with 30 days
            options_daysB=new String[30] ;
            for ( int i=0; i<30;i++){
                options_daysB[i]=formatter.format(i+1);
            }

        }else{
            if(selected_monthB==1){
                // february
                if((thisYear-selected_yearB)%4==0){ // 29 days
                    options_daysB=new String[29] ;
                    for ( int i=0; i<29;i++){
                        options_daysB[i]=formatter.format(i+1);
                    }

                }else{ //28 days
                    options_daysB=new String[28] ;
                    for ( int i=0; i<28;i++){
                        options_daysB[i]=formatter.format(i+1);
                    }

                }
            }else{ // months with 31 days
                options_daysB=new String[31] ;
                for ( int i=0; i<31;i++){
                    options_daysB[i]=formatter.format(i+1);
                }


            }

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_daysB);
        sDayB.setAdapter(adapter);
        if(selected_dayB>=options_daysB.length){
            selected_dayB=0;
        }
        sDayB.setText(sDayB.getAdapter().getItem(selected_dayB).toString(), false);
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_monthB);
        sMonthB.setAdapter(adapter);

        sMonthB.setText(sMonthB.getAdapter().getItem(selected_monthB).toString(), false);
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_yearsB);
        sYearB.setAdapter(adapter);

        sYearB.setText(sYearB.getAdapter().getItem(selected_yearB).toString(), false);

    }
    public void setDaysListB(){
        DecimalFormat formatter = new DecimalFormat("00");
        if(selected_monthB==3 || selected_monthB==5|| selected_monthB==8|| selected_monthB==10){
            // months with 30 days
            options_daysB=new String[30] ;
            for ( int i=0; i<30;i++){
                options_daysB[i]=formatter.format(i+1);
            }

        }else{
            if(selected_monthB==1){
                // february
                if((thisYear-selected_yearB)%4==0){ // 29 days
                    options_daysB=new String[29] ;
                    for ( int i=0; i<29;i++){
                        options_daysB[i]=formatter.format(i+1);
                    }

                }else{ //28 days
                    options_daysB=new String[28] ;
                    for ( int i=0; i<28;i++){
                        options_daysB[i]=formatter.format(i+1);
                    }

                }
            }else{ // months with 31 days
                options_daysB=new String[31] ;
                for ( int i=0; i<31;i++){
                    options_daysB[i]=formatter.format(i+1);
                }


            }

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_down_items,options_daysB);
        sDayB.setAdapter(adapter);
        if(selected_dayB>=options_daysB.length){
            selected_dayB=0;
        }
        sDayB.setText(sDayB.getAdapter().getItem(selected_dayB).toString(), false);
    }

    private void refreshList(){
        try {
            DataManager.getChargements();
        }catch (Exception e){

        }
    }
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChargementsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChargementsList.
     */
    // TODO: Rename and change types and number of parameters
    public static ChargementsList newInstance(String param1, String param2) {
        ChargementsList fragment = new ChargementsList();
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

        refreshList();
        Toast.makeText(mainActivity, "Chargement des rapports ... " ,Toast.LENGTH_SHORT).show();
        chargementList=new ChargementList(mainActivity);
        View view= inflater.inflate(R.layout.fragment_chargements_list, container, false);
        ListView listView = (ListView)view.findViewById(R.id.chargementsList);
        listView.setAdapter(chargementList);

        searchFAB=view.findViewById(R.id.search_button);
        searchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Pressed","On click Rapport search");
                View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_rapport, null);
                Button b= sheetView.findViewById(R.id.validateSearch);
                sYearA=sheetView.findViewById(R.id.dropBoxYearA);
                sMonthA=sheetView.findViewById(R.id.dropBoxMonthA);
                sDayA=sheetView.findViewById(R.id.dropBoxDayA);
                sYearB=sheetView.findViewById(R.id.dropBoxYearB);
                sMonthB=sheetView.findViewById(R.id.dropBoxMonthB);
                sDayB=sheetView.findViewById(R.id.dropBoxDayB);
                routeField = sheetView.findViewById(R.id.routeField);
                routeField .getEditText().setText(chargementList.getFilterRoute());
                setDatesListsA();
                sDayA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                     Log.v("Day ",""+i );
                                                     selected_dayA=i;
                                                 }
                                             }

                );

                sDayA.setText(sDayA.getAdapter().getItem(selected_dayA).toString(), false);
                sMonthA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                   @Override
                                                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                       Log.v("month ",""+i );
                                                       selected_monthA=i;
                                                       setDaysListA();
                                                   }
                                               }

                );
                sMonthA.setText(sMonthA.getAdapter().getItem(selected_monthA).toString(), false);




                sYearA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                      Log.v("Year ",""+i );
                                                      selected_yearA=i;
                                                      setDaysListA();
                                                  }
                                              }

                );
                sYearA.setText(sYearA.getAdapter().getItem(selected_yearA).toString(), false);

                setDatesListsB();
                sDayB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                     Log.v("Day ",""+i );
                                                     selected_dayB=i;
                                                 }
                                             }

                );

                sDayB.setText(sDayB.getAdapter().getItem(selected_dayB).toString(), false);
                sMonthB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                   @Override
                                                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                       Log.v("month ",""+i );
                                                       selected_monthB=i;
                                                       setDaysListB();
                                                   }
                                               }

                );
                sMonthB.setText(sMonthB.getAdapter().getItem(selected_monthB).toString(), false);




                sYearB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                      Log.v("Year ",""+i );
                                                      selected_yearB=i;
                                                      setDaysListB();
                                                  }
                                              }

                );
                sYearB.setText(sYearB.getAdapter().getItem(selected_yearB).toString(), false);

                BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(sheetView);
                dialog.show();

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("search","search");

                        String date_A=""+(thisYear-selected_yearA)+"-"+(selected_monthA+1)+"-"+(selected_dayA+1);
                        String date_B=""+(thisYear-selected_yearB)+"-"+(selected_monthB+1)+"-"+(selected_dayB+1);
                        String route =routeField.getEditText().getText().toString();
                        Log.v("After",date_A);
                        Log.v("Before",date_B);
                        Log.v("Route",route);
                        chargementList.setFilters(date_B,date_A,route);
                        dialog.hide();

                    }
                });
            }
        });
        return view;
    }

    public void setListRequest(ArrayList <Chargement> list) {
        mainActivity.runOnUiThread( new Runnable() {
            public void run() {
                chargementList.setChargementsArray(list);
            }
        });

    }
}