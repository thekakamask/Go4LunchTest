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


    public BaseFragment() {
        // EMPTY PUBLIC CONSTRUCTOR
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActionBar getActionBar() {
        return ((MainActivity) requireActivity()).getSupportActionBar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALLS_ID) {
            checkPermissions();
        }
    }







    private void checkPermissions() {
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

    /*public void onProviderDisabled(String provider) { }

    public void onProviderEnabled(String provider) {
        Log.d("LocationProject", "Provider Enabled");
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
    }*/


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
        checkPermissions();
    }

    protected OnFailureListener onFailureListener() {
        return e -> StyleableToast.makeText(requireContext(), "Unknown Error", R.style.personalizedToast).show();
    }




}
