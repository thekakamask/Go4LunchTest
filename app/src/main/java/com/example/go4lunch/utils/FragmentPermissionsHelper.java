package com.example.go4lunch.utils;

import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

public class FragmentPermissionsHelper {

    /*public void startPermissionRequest(FragmentActivity fA, FragmentPermissionsInterface fPI, String mani) {
        ActivityResultLauncher<String> requestPermissionLauncher =
                fA.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });
        requestPermissionLauncher.launch(
                //Manifest.permission.REQUESTED_PERMISSION);
    }*/

}
