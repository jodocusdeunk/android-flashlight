package com.jodocusdeunk.flashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.RestrictionsManager;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.UserManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button FlashLight;
    private final int CAMERA_REQUEST_CODE=2;
    boolean hasCameraFlash = false;
    private boolean isFlashOn=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        FlashLight =  (Button)findViewById(R.id.Flash);

        FlashLight.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                askPermission(Manifest.permission.CAMERA,CAMERA_REQUEST_CODE);
            }
        });
    }
    private void flashLight() {
        if (hasCameraFlash) {
            if (isFlashOn) {
                FlashLight.setText(this.getString(R.string.bTextOn));
                FlashLight.setBackgroundColor(getResources().getColor(R.color.colorButtonOn));
                FlashLight.setTextColor(this.getColor(R.color.colorButtonTextOn));
                flashLightOff();
                isFlashOn=false;
            } else {
                FlashLight.setText(this.getString(R.string.bTextOff));
                FlashLight.setBackgroundColor(getResources().getColor(R.color.colorButtonOff));
                FlashLight.setTextColor(this.getColor(R.color.colorButtonTextOff));
                flashLightOn();
                isFlashOn=true;
            }
        } else {
            Toast.makeText(MainActivity.this, "No flash available on your device", Toast.LENGTH_SHORT).show();
        }
    }
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
        }
    }
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }
    private void askPermission(String permission,int requestCode) {
        if (ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
            // We Don't have permission
            ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);

        }else {
            // We already have permission do what you want
            flashLight();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasCameraFlash = getPackageManager().
                            hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                    Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                    flashLight();

                } else {
                    Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, this.getString(R.string.title), Toast.LENGTH_LONG).show();
                break;
            case R.id.item2:
                finishAndRemoveTask();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}