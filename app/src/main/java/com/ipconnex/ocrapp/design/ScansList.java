package com.ipconnex.ocrapp.design;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ipconnex.ocrapp.DataManager;
import com.ipconnex.ocrapp.MainActivity;
import com.ipconnex.ocrapp.R;
import com.ipconnex.ocrapp.model.InvoicesList;
import com.ipconnex.ocrapp.model.Invoice;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScansList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScansList extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private InvoicesList invoicesList;
    private MainActivity mainActivity;
    public ScansList() {
        // Required empty public constructor
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void refreshList(){
        try {
            DataManager.getInvoices();
        }catch (Exception e){

        }
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScansList.
     */
    // TODO: Rename and change types and number of parameters
    public static ScansList newInstance(String param1, String param2) {
        ScansList fragment = new ScansList();
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
        Toast.makeText(mainActivity, "Chargement des Factures ... " ,Toast.LENGTH_SHORT).show();
        int id= R.layout.fragment_scans_list;
        View view= inflater.inflate(R.layout.fragment_scans_list, container, false);
        ListView listView = (ListView)view.findViewById(R.id.invoicesList);

        invoicesList = new InvoicesList(mainActivity);
        listView.setAdapter(invoicesList);
        listView.setOnItemClickListener(this);
        return view;


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        invoicesList.getItem(i).setDetailed();
        invoicesList.updateResults();
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, this).commit();
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    public void setListRequest(ArrayList <Invoice> list) {
        mainActivity.runOnUiThread( new Runnable() {
            public void run() {
                invoicesList.setInvoicesArray(list);
            }
        });

    }
}