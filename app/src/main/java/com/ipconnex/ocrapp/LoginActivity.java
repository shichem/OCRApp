package com.ipconnex.ocrapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout user,password ;
    private Button loginButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);


        user =findViewById(R.id.userField);
        password =findViewById(R.id.passewordField);
        loginButton =findViewById(R.id.loginButton);
        SharedPreferences.Editor Ed=sp.edit();
        DataManager.setLoginActivity(this);
        DataManager.setActivateLogin(false);
        String usr = sp.getString(DataManager.USERNAME,"");
        String mdp = sp.getString(DataManager.PASSWORD,"");
        if(usr!="" && mdp!=""){
            try {
                setLoginIsEnabled(false);
                DataManager.senLogin(usr,mdp);
                // goto main activity if correct
            }catch (Exception e){
                //activate login inputs
                setLoginIsEnabled(true);


            }

        }
        /*
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;

                }
            }
        };*/

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   /*finish();

                Intent switchActivityIntent = new Intent(this, SecondActivity.class);
                switchActivityIntent.putExtra("message", "From: " + FirstActivity.class.getSimpleName());
                startActivity(switchActivityIntent);*/
                // TODO check login data
                String userValue = user.getEditText().getText().toString();
                String passwordValue = password.getEditText().getText().toString();
                try {
                    setLoginIsEnabled(false);
                    DataManager.senLogin(userValue,passwordValue);
                } catch (Exception e) {
                    setLoginIsEnabled(true);
                    startToast("Erreur de connection ");
                }
                /*
                Log.v("Ex values : ","( " +sp.getAll().toString() +")");
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString(USERNAME,userValue );
                Ed.putString(PASSWORD,passwordValue);
                Ed.commit();

                Log.v("values : ","( " +sp.getAll().toString() +")");
                */
            }
        });
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Si vous avez des problemes concernant vos données de connexion\n Veuillez contacter votre fournisseur d'application ! ").setPositiveButton("OK", dialogClickListener)
                .show();*/
    }

    public void setLoginIsEnabled(boolean b) {


        runOnUiThread( new Runnable() {
            public void run() {/*
                user.setEnabled(b);
                password.setEnabled(b);
                loginButton.setEnabled(b);*/
            }

        });


    }
    public void login(String usr, String mdp){
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString(DataManager.USERNAME,usr);
        Ed.putString(DataManager.PASSWORD,mdp);
        Ed.apply();
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    public void startToast(String message) {
        LoginActivity m = this ;
        runOnUiThread( new Runnable() {
            public void run() {
                Toast.makeText(m, message ,Toast.LENGTH_SHORT).show();

            }
        });
    }
}