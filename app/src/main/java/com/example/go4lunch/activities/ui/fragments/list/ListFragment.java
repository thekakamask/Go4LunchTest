package com.example.go4lunch.activities.ui.fragments.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.RestaurantActivity;
import com.example.go4lunch.activities.ui.fragments.BaseFragment;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetail;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetailsResult;
import com.example.go4lunch.repository.StreamRepository;
import com.example.go4lunch.utils.ItemClickSupport;
import com.example.go4lunch.viewModels.StreamViewModel;
import com.example.go4lunch.views.ListFragmentAdapter;
import com.example.go4lunch.views.ListViewHolder;
import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class ListFragment extends BaseFragment implements Serializable {


    /*@Override
    protected BaseFragment newInstance() {
        return new ListFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map;
    }*/

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.RV_listFragment)
    RecyclerView mRecyclerView;
    public List<PlaceDetail> placeDetails;
    private ListFragmentAdapter mAdapter;
    private String mPosition;
    public Disposable mDisposable;
    private final float[] distanceResults = new float[3];
    private StreamViewModel streamViewModel;
    //I DONT DIRECTLY CALL STREAM REPOSITORY SO I WILL USE STREAMVIEWMODEL

    public ListFragment() {
        // EMPTY PUBLIC CONSTRUCTOR
    }


    /*public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        //SEARCHVIEW
        setHasOptionsMenu(true);

        //INIT VIEWMODEL WITH PROVIDERS
        streamViewModel = new ViewModelProvider(this).get(StreamViewModel.class);

        this.configureRV();
        this.configureOnClickRV();
        return view;


    }


    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        getActionBar().setTitle(R.string.listFragment_bar);

        if (placeDetails != null && placeDetails.size()==0) {

            executeHttpRequestWithRetrofit();
        }
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle(R.string.listFragment_bar);
    }*/

    @Override
    public void onCreateOptionsMenu(@androidx.annotation.NonNull Menu menu, @androidx.annotation.NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_activity_action_search, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*if (newText.isEmpty()) {
                    executeHttpRequestWithRetrofit();
                }*/
                executeHttpRequestWithRetrofitAutocomplete(newText);
                return true;
            }
        });
    }


    private void executeHttpRequestWithRetrofit() {
        //this.mDisposable = StreamRepository.streamFetchRestaurantDetails(mPosition, 3000, "restaurant|cafe|bakery|bar|meal_takeaway|meal_delivery")
        // I USE INSTEAD LINE 151

        this.mDisposable = streamViewModel.getStreamFetchRestaurantDetails(mPosition, 3000, "restaurant|cafe|bakery|bar|meal_takeaway|meal_delivery").getValue()
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {
                    @Override
                    public void onSuccess(@NonNull List<PlaceDetail> placeDetails) {
                        Collections.sort(placeDetails, new Comparator<PlaceDetail>() {

                            @Override
                            public int compare(PlaceDetail o1, PlaceDetail o2) {
                                return o1.getResult().getName().compareTo(o2.getResult().getName());
                            }
                        });

                        updateUI(placeDetails);

                        Log.d("TestPlaceDetail", String.valueOf(placeDetails.size()));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TestPlaceDetail", Log.getStackTraceString(e));

                    }
                });

    }

    private void executeHttpRequestWithRetrofitAutocomplete(String input) {
        //this.mDisposable = StreamRepository.streamFetchAutocompleteInfos(input, 2000, mPosition, "establishment") INSTEAD I USE LINE 180

        this.mDisposable = streamViewModel.getStreamFetchAutoCompleteInfos(input, 2000, mPosition, "establishment").getValue()
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>(){


                    @Override
                    public void onSuccess(@NonNull List<PlaceDetail> placeDetails) {
                        updateUIAutoComplete(placeDetails);

                        Log.d("TestPlaceDetail", String.valueOf(placeDetails.size()));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TestAutocomplete", Log.getStackTraceString(e));
                    }
                });

    }


    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(List<PlaceDetail> placeDetails) {

        //this.placeDetails.clear();

        //int distance = Math.round(distanceResults[0]);


        //Collections.sort(placeDetails,Comparator.comparing(o -> o.getResult().getName()));

        /*placeDetails
                .stream()
                .sorted(Comparator.comparing(object -> object.getResult().getName()));*/

        this.placeDetails.addAll(placeDetails);





        Log.d("TestUI", placeDetails.toString());
        mAdapter.notifyDataSetChanged();
    }

    /*private void restaurantDistance(String startLocation, com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetailsLocation endLocation ) {
        String[] separatedStart = startLocation.split(",");
        double startLatitude= Double.parseDouble(separatedStart[0]);
        double startLongitude=Double.parseDouble(separatedStart[1]);
        double endLatitude=endLocation.getLat();
        double endLongitude=endLocation.getLng();
        android.location.Location.distanceBetween(startLatitude,startLongitude, endLatitude,endLongitude,distanceResults);

    }*/

    @SuppressLint("NotifyDataSetChanged")
    private void updateUIAutoComplete(List<PlaceDetail> placeDetails) {

        this.placeDetails.clear();
        this.placeDetails.addAll(placeDetails);

        Log.d("TestUI", placeDetails.toString());
        mAdapter.notifyDataSetChanged();
    }







    private void configureRV() {
        //RESET LIST
        this.placeDetails= new ArrayList<>();
        //CREATION OF THE ADAPTER PASSING THE RESTAURANTS LIST
        this.mAdapter = new ListFragmentAdapter(this.placeDetails
                , Glide.with(this)
                , this.mPosition);

        //ADAPTER TO RV TO ITEMS
        this.mRecyclerView.setAdapter(mAdapter);
        // SET LAYOUTMANAGER WITH POSITON TO ITEMS
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }
    private void configureOnClickRV() {
        ItemClickSupport.addTo(mRecyclerView,R.layout.fragment_list_item)
                .setOnItemClickListener(((mRecyclerView, mPosition, v) -> {

                    PlaceDetailsResult placeDetailsResult = placeDetails.get(mPosition).getResult();
                    Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("placeDetailsResult", placeDetailsResult);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }));

        ItemClickSupport.addTo(mRecyclerView,R.layout.fragment_list_item)
                .setOnItemLongClickListener(((mRecyclerView, mPosition, v) -> {

                    PlaceDetailsResult placeDetailsResult = placeDetails.get(mPosition).getResult();
                    Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("placeDetailsResult", placeDetailsResult);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return false;
                }));

    }

    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();
        mPosition = mLatitude + "," + mLongitude;
        Log.d("TestListPosition", mPosition);
        mAdapter.setPosition(mPosition);
        executeHttpRequestWithRetrofit();
    }


    @Override
    public void onResume() {
        super.onResume();
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
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }


}