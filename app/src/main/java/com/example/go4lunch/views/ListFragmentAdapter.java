package com.example.go4lunch.views;

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

public class ListFragmentAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private String mPosition;
    private final RequestManager glide;
    private final List<PlaceDetail> placeDetails;


    public void setPosition(String position) {
        mPosition = position;
    }

    public ListFragmentAdapter(List<PlaceDetail> placeDetails, RequestManager glide, String mPosition ) {
        this.placeDetails = placeDetails;
        this.glide = glide;
        this.mPosition = mPosition ;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.updateWithDetails(this.placeDetails.get(position).getResult(), this.glide, this.mPosition);
    }

    @Override
    public int getItemCount() {
        return this.placeDetails.size();
    }
}




