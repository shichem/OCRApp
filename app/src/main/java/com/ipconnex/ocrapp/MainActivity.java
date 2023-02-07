package com.ipconnex.ocrapp;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.textfield.TextInputLayout;
import com.ipconnex.ocrapp.databinding.ActivityMainBinding;
import com.ipconnex.ocrapp.design.AddChargement;
import com.ipconnex.ocrapp.design.AddScan;
import com.ipconnex.ocrapp.design.ChargementsList;
import com.ipconnex.ocrapp.design.ScansList;
import com.ipconnex.ocrapp.design.Settings;
import com.websitebeaver.documentscanner.DocumentScanner;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    public AddScan addScan=new AddScan();
    public ScansList scansList=new ScansList();
    public AddChargement addChargement=new AddChargement();
    public ChargementsList chargementsList=new ChargementsList();
    public Settings settings=new Settings();

    public BottomNavigationView bottomNavigationView;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ImageView croppedImageView;
        /*
    DocumentScanner documentScanner = new DocumentScanner(
            this,
            (croppedImageResults) -> {
                // display the first cropped image
                croppedImageView.setImageBitmap(
                        BitmapFactory.decodeFile(croppedImageResults.get(0))
                );
                //croppedImageView img json
                try {
                    DataManager.sendInvoice(croppedImageResults.get(0));
                    DataManager.getInvoices();
                }catch (Exception e){
                    Log.v("Error",e.getMessage());
                }
                return null;
            },
            (errorMessage) -> {
                // an error happened
                Log.v("documentscannerlogs", errorMessage);
                return null;
            },
            () -> {
                // user canceled document scan
                Log.v("documentscannerlogs", "User canceled document scan");
                return null;
            },
            null,
            null,
            null
    );*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item-> {
                    switch (item.getItemId()) {
                        case R.id.scan:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, addScan).commit();
                            CameraScanActivity.setType(CameraScanActivity.SEND_FACTURE);
                            return true;

                        case R.id.scans_list:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, scansList).commit();
                            CameraScanActivity.setType(CameraScanActivity.SEND_FACTURE);
                            return true;
                        case R.id.addChargement:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, addChargement).commit();
                            CameraScanActivity.setType(CameraScanActivity.SEND_CHARGEMENT);
                            return true;

                        case R.id.chargementsList:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment,chargementsList).commit();
                            CameraScanActivity.setType(CameraScanActivity.SEND_CHARGEMENT);
                            return true;

                        case R.id.settings:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, settings).commit();
                            return true;
                    }
                    return false;
                }
        );
        bottomNavigationView.setSelectedItemId(R.id.scan);
        DataManager.setMainActivity(this);
        scansList.setMainActivity(this);
        chargementsList.setMainActivity(this);
        /*
        // cropped image
*/

    }
    public void toasterInternetError(){

        Toast.makeText(this, "Erreur du serveur ... " ,Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment_content_main );
        return NavigationUI.navigateUp( navController, appBarConfiguration )
                || super.onSupportNavigateUp();
    }

    public void setRecivedInvoice(String json) {
        runOnUiThread( new Runnable() {
            public void run() {

                try{
                    JSONObject data = new JSONObject(json);

                    addScan.setData(data.getString("image"),data.getString("id_facture"),
                            data.getString("id_magasin"),
                            data.getString("id_client"),
                            data.getString("t_vendu"),
                            data.getString("t_retour"),
                            data.getString("total"),
                            data.getString("qte"),
                            data.getString("date"));
                    /* String image = jInvoice.getString("image") ; */

                }catch (Exception e){
                    //parsing error

                }


            }
        });

    }
    public void setRecivedRapport(String json) {
        runOnUiThread( new Runnable() {
            public void run() {

                try{
                    JSONObject data = new JSONObject(json);
                    addChargement.setData(data.getString("image")
                            ,data.getString("route"),
                            data.getString("date"),
                            data.getString("liste_produits"));

                }catch (Exception e){
                    //parsing error

                }


            }
        });
    }
    public void startToast(String message) {
        MainActivity m = this ;
        runOnUiThread( new Runnable() {
            public void run() {
                Toast.makeText(m, message ,Toast.LENGTH_LONG).show();

            }
        });
    }

    public void clearData() {

        runOnUiThread( new Runnable() {
            public void run() {
                Date date = new Date();
                SimpleDateFormat year_formatter = new SimpleDateFormat("yyyy");
                Integer thisYear = new Integer(year_formatter.format(date));


                if(CameraScanActivity.getType()==CameraScanActivity.SEND_FACTURE){

                    addScan.setData("","","","","","","","","");
                    bottomNavigationView.setSelectedItemId(R.id.scans_list);
                }
                if(CameraScanActivity.getType()==CameraScanActivity.SEND_CHARGEMENT){
                    addChargement.setData("","","01/01/"+thisYear,"");
                    bottomNavigationView.setSelectedItemId(R.id.chargementsList);
                }
            }
        });

    }

    public void setChargementIsEnabled(boolean b) {


        runOnUiThread( new Runnable() {
            public void run() {
                addChargement.setIsEnabled(b);
            }

        });


    }
    public void setAddScanIsEnabled(boolean b) {


        runOnUiThread( new Runnable() {
            public void run() {
                addScan.setIsEnabled(b);
            }

        });


    }


}