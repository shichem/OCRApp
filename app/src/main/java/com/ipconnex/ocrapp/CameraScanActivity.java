package com.ipconnex.ocrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.websitebeaver.documentscanner.DocumentScanner;

public class CameraScanActivity extends AppCompatActivity {

    private ImageView croppedImageView;

    DocumentScanner documentScanner = new DocumentScanner(
            this,
            (croppedImageResults) -> {
                // display the first cropped image
                /*
                croppedImageView.setImageBitmap(
                        BitmapFactory.decodeFile(croppedImageResults.get(0))
                );*/
                //croppedImageView img json
                try {
                    DataManager.sendInvoice(croppedImageResults.get(0));
                }catch (Exception e){
                    Log.v("Error",e.getMessage());
                }
                finish();
                return null;
            },
            (errorMessage) -> {
                // an error happened
                Log.v("documentscannerlogs", errorMessage);
                return null;
            },
            () -> {
                Log.v("documentscannerlogs", "User canceled document scan");

                this.finish();
                return null;
            },
            null,
            null,
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