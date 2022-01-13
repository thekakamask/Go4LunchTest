package com.example.go4lunch.activities.ui.fragments.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.RestaurantActivity;
import com.example.go4lunch.activities.ui.fragments.BaseFragment;
import com.example.go4lunch.activities.ui.fragments.list.ListFragment;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetail;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetailsResult;
import com.example.go4lunch.repository.StreamRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.api.LogDescriptor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;



public class MapFragment extends BaseFragment implements LocationListener, Serializable {

    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private Disposable mDisposable;
    private String mPosition;
    private Marker positionMarker;
    private static final String TAG = "MapFragment";



    public MapFragment() {
        //EMPTY PUBLIC CONSTRUCTOR
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        //for SearchView
        setHasOptionsMenu(true);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        mMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        getActionBar().setTitle(R.string.title_bar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_activity_actionsearch, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    executeHttpRequestWithRetrofit();
                }
                executeHttpRequestWithRetrofitAutocomplete(newText);
                return true;
            }
        });


    }

    private void executeHttpRequestWithRetrofit() {
        this.mDisposable = StreamRepository.streamFetchRestaurantDetails(mPosition, 3000, "restaurant")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {
                    @Override
                    public void onSuccess(List<PlaceDetail> placeDetails) {
                        positionMarker(placeDetails);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("TestDetail", Log.getStackTraceString(e));
                    }
                });

        mGoogleMap.setOnInfoWindowClickListener(marker -> {
            //RETRIEVING RESULT
            PlaceDetailsResult positionMarkerList = (PlaceDetailsResult) positionMarker.getTag();
            Intent intent = new Intent(getContext(), RestaurantActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("placeDetailsResult", positionMarkerList);
            intent.putExtras(bundle);
            startActivity(intent);

        });
    }

    private void executeHttpRequestWithRetrofitAutocomplete(String input) {

        this.mDisposable = StreamRepository.streamFetchAutocompleteInfos(input, 2000, mPosition, "establishment")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<PlaceDetail> placeDetails) {
                        positionMarker(placeDetails);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TestAutocomplete", Log.getStackTraceString(e));
                    }
                });
        mGoogleMap.setOnInfoWindowClickListener(marker -> {
            //RETRIEVING RESULT
            PlaceDetailsResult positionMarkerList = (PlaceDetailsResult) positionMarker.getTag();
            Intent intent = new Intent (getContext(),RestaurantActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("placeDetailsResult", positionMarkerList);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();

        if (mGoogleMap != null) {
            LatLng googleLocation = new LatLng(mLatitude, mLongitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            mPosition = mLatitude + "," + mLongitude;
            Log.d("TestLatLng", mPosition);
            executeHttpRequestWithRetrofit();
        }
    }

    private void positionMarker(List<PlaceDetail> placeDetails) {
        mGoogleMap.clear();
        for (PlaceDetail detail : placeDetails) {
            LatLng latLng = new LatLng(detail.getResult().getGeometry().getLocation().getLat(),
                    detail.getResult().getGeometry().getLocation().getLng());
            positionMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_marker_100dp))
                    .title(detail.getResult().getName())
                    .snippet(detail.getResult().getVicinity()));
            positionMarker.showInfoWindow();
            PlaceDetailsResult placeDetailsResult = detail.getResult();
            positionMarker.setTag(placeDetailsResult);
            Log.d("detailResultMap", String.valueOf(placeDetailsResult));
        }
    }

    private void loadMap() {
        mMapFragment.getMapAsync(googleMap -> {
            Log.d(TAG, "loadMap: ");
            mGoogleMap = googleMap;
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) getContext(),new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMS_CALLS_ID);
                return;
            }
            googleMap.setMyLocationEnabled(true);
        });
    }





    @Override
    public void onResume() {
        super.onResume();
        loadMap();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }
}
