package com.example.go4lunch.activities.ui.fragments.coworkers;

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

import com.example.go4lunch.R;

public class CoworkersFragment extends Fragment {

    private CoworkersViewModel mCoworkersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        mCoworkersViewModel =
                new ViewModelProvider(this).get(CoworkersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_coworkers, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        mCoworkersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}