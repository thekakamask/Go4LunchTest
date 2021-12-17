package com.example.go4lunch.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.go4lunch.R;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetail;

import java.util.List;

public class Go4lunchAdapter extends RecyclerView.Adapter<Go4LunchViewHolder> {

    private String mPosition;
    private RequestManager glide;
    private List<PlaceDetail> placeDetails;

    public Go4lunchAdapter(List<PlaceDetail> placeDetails, RequestManager glide, String mPosition) {
        this.placeDetails = placeDetails;
        this.glide = glide;
        this.mPosition = mPosition;
    }

    public void setPosition(String position) {
        mPosition = position;
    }


    @NonNull
    @Override
    public Go4LunchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, parent, false);

        return new Go4LunchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Go4LunchViewHolder viewHolder, int position) {
        viewHolder.updateWithDetails(this.placeDetails.get(position).getResult(), this.glide, this.mPosition);

    }

    @Override
    public int getItemCount() {
        return this.placeDetails.size();
    }
}
