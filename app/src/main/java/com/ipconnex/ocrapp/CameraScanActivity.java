package com.ipconnex.ocrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.websitebeaver.documentscanner.DocumentScanner;
import com.websitebeaver.documentscanner.constants.ResponseType;

public class CameraScanActivity extends AppCompatActivity {
    public static final int SEND_FACTURE=0,SEND_CHARGEMENT=1;
    private static int type=0;
    private ImageView croppedImageView;

    public static void setType(int type) {
        CameraScanActivity.type = type;
    }

    public static int getType() {
        return type;
    }

    DocumentScanner documentScanner = new DocumentScanner(
            this,
            (croppedImageResults) -> {
                // display the first cropped image
                /*
                croppedImageView.setImageBitmap(
                        BitmapFactory.decodeFile(croppedImageResults.get(0))
                );*/
                //croppedImageView img json
                if(type==CameraScanActivity.SEND_CHARGEMENT){
                    try {
                        DataManager.sendRapport(croppedImageResults.get(0));
                        DataManager.setImageText("rap","Chargement ...");
                    }catch (Exception e){
                        Log.v("Error",e.getMessage());


                        DataManager.cancelLoading();
                    }



                }else{
                    try {
                        DataManager.setImageText("inv","Chargement ...");
                        DataManager.sendInvoice(croppedImageResults.get(0));
                    }catch (Exception e){
                        Log.v("Error",e.getMessage());

                        DataManager.cancelLoading();
                    }

                }

                finish();
                return null;
            },
            (errorMessage) -> {
                // an error happened
                Log.v("documentscannerlogs", errorMessage);
                DataManager.cancelLoading();
                return null;
            },
            () -> {
                Log.v("documentscannerlogs", "User canceled document scan");


                this.finish();
                DataManager.cancelLoading();

                return null;
            },
            ResponseType.IMAGE_FILE_PATH,
            true,
            null
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataManager.setCameraScanActivity(this);
        documentScanner.startScan();
        setContentView(R.layout.activity_camera_scan);
    }
}