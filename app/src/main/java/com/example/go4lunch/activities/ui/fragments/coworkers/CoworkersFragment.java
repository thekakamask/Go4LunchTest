package com.example.go4lunch.activities.ui.fragments.coworkers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.fragments.BaseFragment;
import com.example.go4lunch.models.User;
import com.example.go4lunch.views.CoworkersFragmentAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CoworkersFragment extends BaseFragment {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fragment_Coworkers_RV)
    RecyclerView mRecyclerViewCoworkers;

    private CoworkersFragmentAdapter mCoworkersFragmentAdapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionUsers = db.collection("users");


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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActionBar().setTitle(R.string.available_coworkers);
    }

    private void setUpRecyclerView() {

        Query query = collectionUsers.orderBy("placeId", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        this.mCoworkersFragmentAdapter = new CoworkersFragmentAdapter(options, Glide.with(this));
        mRecyclerViewCoworkers.setHasFixedSize(true);
        mRecyclerViewCoworkers.setAdapter(mCoworkersFragmentAdapter);
        mRecyclerViewCoworkers.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCoworkersFragmentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCoworkersFragmentAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}