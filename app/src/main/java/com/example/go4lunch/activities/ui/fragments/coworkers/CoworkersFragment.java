package com.example.go4lunch.activities.ui.fragments.coworkers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.fragments.BaseFragment;
import com.example.go4lunch.activities.ui.fragments.map.MapFragment;
import com.example.go4lunch.models.User;
import com.example.go4lunch.utils.CoworkersAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.disposables.Disposable;

public class CoworkersFragment extends BaseFragment {

    @BindView(R.id.fragment_Coworkers_RV)
    RecyclerView mRecyclerViewCoworkers;

    private Disposable mDisposable;
    private CoworkersAdapter mCoworkersAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionUsers = db.collection("users");


    public CoworkersFragment() {
        //EMPTY PUBLIC CONSTRUCTOR
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coworkers, container, false);
        ButterKnife.bind(this, view);

        setUpRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActionBar().setTitle(R.string.avalaible_coworkers);
    }

    private void setUpRecyclerView() {
        Query query = collectionUsers.orderBy("placeId", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        this.mCoworkersAdapter = new CoworkersAdapter(options, Glide.with(this));
        mRecyclerViewCoworkers.setHasFixedSize(true);
        mRecyclerViewCoworkers.setAdapter(mCoworkersAdapter);
        mRecyclerViewCoworkers.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCoworkersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCoworkersAdapter.stopListening();
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