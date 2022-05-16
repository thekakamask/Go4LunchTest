package com.example.go4lunch.activities.ui.fragments.map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.RestaurantActivity;
import com.example.go4lunch.activities.ui.fragments.BaseFragment;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetail;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetailsResult;
import com.example.go4lunch.repository.StreamRepository;
import com.example.go4lunch.viewModels.StreamViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.Serializable;
import java.util.List;
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
    private StreamViewModel streamViewModel;



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

        //INIT VIEWMODEL WITH PROVIDERS
        streamViewModel = new ViewModelProvider(this).get(StreamViewModel.class);

        //for SearchView
        setHasOptionsMenu(true);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        mMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        getActionBar().setTitle(R.string.title_bar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_activity_action_search, menu);
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
        Log.d(TAG, "executeHttpRequestWithRetrofit: COUCOU");
        //StreamRepository.streamFetchRestaurantDetails(mPosition,3000, "restaurant");
        this.mDisposable = StreamRepository.streamFetchRestaurantDetails(mPosition, 3000, "restaurant|cafe|bakery|bar|meal_takeaway|meal_delivery")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {
                    @Override
                    public void onSuccess(List<PlaceDetail> placeDetails) {
                        positionMarker(placeDetails);
                        Log.d(TAG, "httprequestretrofitOnSuccess" + placeDetails.size());
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"TestDetail" + Log.getStackTraceString(e));
                    }
                });
        Log.d(TAG, "executeHttpRequestWithRetrofitDisposableTest: " + this.mDisposable);


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
        Log.d(TAG, "onLocationChanged: " + location.toString());
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();
        Log.d(TAG, "onLocationChanged: (mGoogleMap==null) " +(mGoogleMap==null));
        if (mGoogleMap != null) {
            LatLng googleLocation = new LatLng(mLatitude, mLongitude);
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLocation, 15));
            mPosition = mLatitude + "," + mLongitude;
            Log.d(TAG, "onLocationChanged: mPosition" + mPosition);
            executeHttpRequestWithRetrofit();
        }
    }

    private void positionMarker(List<PlaceDetail> placeDetails) {
        Log.d(TAG, "positionMarker: ");
        mGoogleMap.clear();
        for (PlaceDetail detail : placeDetails) {
            LatLng latLng = new LatLng(detail.getResult().getGeometry().getLocation().getLat(),
                    detail.getResult().getGeometry().getLocation().getLng());
            positionMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_fragment_restaurant_marker))
                    .title(detail.getResult().getName())
                    .snippet(detail.getResult().getVicinity()));
            assert positionMarker != null;
            positionMarker.showInfoWindow();
            PlaceDetailsResult placeDetailsResult = detail.getResult();
            positionMarker.setTag(placeDetailsResult);
        }
    }

    private void loadMap() {
        mMapFragment.getMapAsync(googleMap -> {
            Log.d(TAG, "loadMap: ");
            mGoogleMap = googleMap;
            Log.d(TAG, "loadMap: (mGoogleMap==null) " + false);
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) requireContext(),new String[] {
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
