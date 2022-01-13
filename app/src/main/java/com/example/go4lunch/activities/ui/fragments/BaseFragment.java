package com.example.go4lunch.activities.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.MainActivity;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Objects;

import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public abstract class BaseFragment extends Fragment implements LocationListener {

   protected static final int PERMS_CALLS_ID=200;
   public LocationManager mLocationManager;
   private GoogleMap mMap;
   private String mPosition;

   //VARIABLES NEEDED FOR ACTIVITY RESULTLAUNCHER
   final String[] PERMISSIONS = {
           Manifest.permission.ACCESS_FINE_LOCATION,
           Manifest.permission.ACCESS_COARSE_LOCATION
   };

   //BEGIN STANDARD CALL SPECIFICATION AND PREDEFINED TO AN ACTIVITY INITIALISED IN THE ONCREATE
    private ActivityResultContracts.RequestMultiplePermissions mRequestMultiplePermissionsContract;
    //LAUNCHER SPECIFICATION FOR A PREVIOUSLY PREPARED CALL TO START THE PROCESS OF EXECUTING AN ACTIVITYRESULT CONTRACT
    //INITIALISED ON THE ONCREATE
    private ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher;
    //END ZONE FOR NECESSAIRELY VARIABLES OF ACTIVITYRESULTLAUCHER


    public BaseFragment() {
        // EMPTY PUBLIC CONSTRUCTOR
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // BEGIN INITIALISED OBJECTS ACTIVITYRESULTCONTRACTS AND ACTIVITYRESULTLAUNCHER
        mRequestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionActivityResultLauncher = registerForActivityResult(mRequestMultiplePermissionsContract, isGranted -> {
                    Log.d(TAG, "launcher result :" + isGranted.toString());
                    if (isGranted.containsValue(false)) {
                        Log.d(TAG, "At least one of the permissions was not granted, launching again...");
                        multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
                    }
                });
        //CHECKING PERMISSIONS
        askPermissions(PERMISSIONS);
    }

    private void askPermissions(String [] PERMISSIONS) {
        //IF PERMISSIONS ARE NOT ACCORDED
        if(!hasPermissions(PERMISSIONS)) {
            Log.d(TAG, "Launching multiple contract permission launcher for ALL required permissions ");
            multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
        } else {
            Log.d(TAG, "All permissions are already granted");
            //ON CONTINUE
            handleGPS();
        }
    }

    private boolean hasPermissions(String[] permissions) {
        if(permissions != null) {
            //BROWSE PERMISSIONS
            for (String permission : permissions) {
                //CONTEXT DEFINITION
                Context context = requireContext();
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED ) {
                    Log.d(TAG, "Permission is not granted" + permission);
                    return false;
                }
                Log.d(TAG, "permission already granted:" + permission);
            }
            return true;
        }
        return false;
    }

    private void handleGPS() {
        //IF WE HAVE PERMISSIONS, WE CONTINUE
        Context context = requireContext();
        mLocationManager = (LocationManager) Objects.requireNonNull(requireContext())
                .getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        //VERIFICATION OF THE PERMISSIONS ARE REQUIRED, IF NOT : ERROR
        if(ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermissions(PERMISSIONS);
            return;
        }
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,15000,10, (LocationListener) this);
            Log.e("GPSProvider", "testGps");
        } else if (mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, 15000, 10, (LocationListener) this);
            Log.e("PassiveProvider", "testPassive");
        } else if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 10,(LocationListener) this);
            Log.e("NetworkProvider", "testNetwork");
        }
    } //HANDLE GPS

    public ActionBar getActionBar() {
        return ((MainActivity) requireActivity()).getSupportActionBar();
    }


    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();

        if (mMap != null) {
            LatLng googleLocation = new LatLng(mLatitude, mLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            mPosition = mLatitude + "," + mLongitude;
            Log.d("TestLatLng", mPosition);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        askPermissions(PERMISSIONS);
    }

    protected OnFailureListener onFailureListener() {
        return e -> StyleableToast.makeText(requireContext(), "Unknown Error", R.style.personalizedToast).show();
    }

    /*public void onProviderDisabled(String provider) { }

    public void onProviderEnabled(String provider) {
        Log.d("LocationProject", "Provider Enabled");
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALLS_ID) {
            checkPermissions();
        }
    }*/







    /*private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALLS_ID);
            return;
        }
        mLocationManager = (LocationManager) Objects.requireNonNull(requireContext())
                .getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 15000, 10, (LocationListener) this);
            Log.e("GPSProvider", "testGPS");
        } else if (mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,15000, 10, (LocationListener) this);
            Log.e("PassiveProvider", "testPassive");
        } else if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 10, (LocationListener) this);
            Log.e("NetWorkProvider", "testNetwork");
        }
    }*/




}
