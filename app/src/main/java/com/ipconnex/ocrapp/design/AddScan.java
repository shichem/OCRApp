package com.ipconnex.ocrapp.design;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ipconnex.ocrapp.CameraScanActivity;
import com.ipconnex.ocrapp.DataManager;
import com.ipconnex.ocrapp.R;
import com.websitebeaver.documentscanner.DocumentScanner;

import java.util.ArrayList;

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
    private TextInputLayout input_t_vendu;
    private TextInputLayout input_t_retour;
    private TextInputLayout input_total;
    private Intent intent;
    private View buttonSendInv ;
    private View buttonStartCam;

    public void setData(String img , String facture ,String magasin ,String client,String t_vendu ,String t_retour ,String total ){

        this.image = img ;
        Log.v(magasin ,img+" "+facture);
        this.input_facture.getEditText().setText(facture);
        this.input_client.getEditText().setText(client);
        this.input_magasin.getEditText().setText(magasin);
        this.input_t_vendu.getEditText().setText(t_vendu);
        this.input_t_retour.getEditText().setText(t_retour);
        this.input_total.getEditText().setText(total);
        setIsEnabled(true);
    }

    public ArrayList<String> getData(){

        ArrayList<String> result = new ArrayList<String>();
        result.add(image);
        result.add(input_facture.getEditText().getText().toString());
        result.add(input_magasin.getEditText().getText().toString());
        result.add(input_client.getEditText().getText().toString());
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_scan.
     */
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
        intent= new Intent(getActivity(), CameraScanActivity.class);

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
                String client= data.get(3);
                String t_retour= data.get(4);
                String t_vendu= data.get(5);
                String total= data.get(6);
                try{
                    setIsEnabled(false);
                    DataManager.sendForm(image,facture,magasin,client,t_vendu,t_retour,total);
                }catch (Exception e ){
                }
            }
        });
        buttonStartCam.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO set Editable false
                setIsEnabled(false);
                // TODO start new Activity
                startActivity(intent);
            }
        });
        return inflate;
    }
    public void setIsEnabled(boolean b ){
        input_facture.setEnabled(b);
        input_client.setEnabled(b);
        input_magasin.setEnabled(b);
        input_t_vendu.setEnabled(b);
        input_t_retour.setEnabled(b);
        input_total.setEnabled(b);
        buttonStartCam.setEnabled(b);
        buttonSendInv.setEnabled(b);

    }
}