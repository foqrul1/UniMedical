package com.example.myapplication.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * Created by Ashiq on 6/1/16.
 */
public class PermissionUtils {

    public static final int REQUEST_WRITE_STORAGE_DOWNLOAD = 112;
    public static final int REQUEST_WRITE_STORAGE_UPLOAD = 113;
    public static final int REQUEST_LOCATION = 116;
    public static final int REQUEST_CALL = 114;
    public static final int REQUEST_WRITE_STORAGE = 115;
    // permission to write sd card
    public static String[] SD_WRITE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    // permission to make a phone call
    public static String[] CALL_PERMISSIONS = {
            Manifest.permission.CALL_PHONE
    };
    public static String[] LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    // permission to record audio
    public static String[] RECORD_AUDIO_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean isPermissionGranted(Activity activity, String[] permissions, int requestCode) {
        boolean requirePermission = false;
        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                if ((ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)) {
                    requirePermission = true;
                    break;
                }
            }
        }

        if (requirePermission) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isPermissionResultGranted(int[] grantResults) {
        boolean allGranted = true;
        if (grantResults != null && grantResults.length > 0) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
        }
        return allGranted;
    }

}
