package com.example.go4lunch.activities.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.example.go4lunch.viewModels.UserViewModel;

//ACTIVITY CLASS THAT ALLOW TO MANAGE ALL THE COMMON CODE FOR THE ACTIVITIES
// <T> SHOULD BE THE TYPE OF THE VIEWBINDING OF YOUR ACTIVITY
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    abstract T getViewBinding();
    protected T binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
    }

    //INIT THE BINDING OBJECT AND THE LAYOUT OF THE ACTIVITY
    private void initBinding() {
        binding = getViewBinding();
        View view = binding.getRoot();
        setContentView(view);
    }

}
