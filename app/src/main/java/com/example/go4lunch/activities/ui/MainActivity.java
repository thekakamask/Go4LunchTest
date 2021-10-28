package com.example.go4lunch.activities.ui;

import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.DragAndDropPermissions;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.fragments.coworkers.CoworkersFragment;
import com.example.go4lunch.activities.ui.fragments.map.MapFragment;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repository.UserRepository;
import com.example.go4lunch.utils.UserManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_bottom)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.main_activity_nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.main_activity_drawer_layout)
    DrawerLayout mDrawerLayout;



    private Disposable mDisposable;
    private String idResto;
    private static final int SIGN_OUT_TASK = 100;

    //private DrawerLayout drawerLayout; USE FINDVIEW BY ID METHOD
    //private NavigationView navigationView; USE FINDVIEW BY ID METHOD
    /*  private Disposable mDisposable;
    private PlaceDetail detail;
    private String idResto;   */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // A°6 CONFIGURE ALL VIEWS
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureUINavHeader();

        //FOR BOTTOM NAVIGATION VIEW
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);


        //MAPFRAGMENT CONNECTION WITH ACTIVITY
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame_layout,
                new MapFragment()).commit();

        //ACTION BAR TITLE
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_bar);
        }

    }

    // A°1 CONFIGURE TOOLBAR
    private void configureToolbar() {
        setSupportActionBar(mToolbar);
    }

    // A°2 CONFIGURE DRAWER LAYOUT
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar,
                R.string.main_activity_navigation_drawer_open,
                R.string.main_activity_navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    /* USE FIND VIEW BY ID METHOD
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.main_activity_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, mToolbar,
                R.string.main_activity_navigation_drawer_open,
                R.string.main_activity_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }*/

    // A°3 CONFIGURE NAVIGATION VIEW
    private void configureNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    //USE FINDVIEW BY ID METHOD
    /*private void configureNavigationView() {
        this.navigationView = findViewById(R.id.main_activity_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // A°4 HANDLE NAVIGATION ITEM CLICK
        int id = item.getItemId();
        switch(id) {
            case R.id.lunch_menu_drawer :
                if(UserManager.getCurrentUser() != null) {
                    UserManager.getInstance().getUserData().addOnSuccessListener(documentSnapshot -> {
                        User user =documentSnapshot.toObject(User.class);
                        if (Objects.requireNonNull(user).getIdOfPlace() != null) {
                            userResto(user);

                        }else {
                            showSnackBar(getString(R.string.no_restaurant_choose));

                        }
                    });


                }
                break;
            case R.id.settings_menu_drawer :
                Intent settingIntent = new Intent (this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.logout_menu_drawer :
                signOutFromUserFirebase();
                showSnackBar(getString(R.string.deconnexion));
                break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void userResto(User users) {
        idResto = users.getIdOfPlace();
        executeHttpResquestWithRetrofit();
    }

    private void executeHttpResquestWithRetrofit() {
       // this.mDisposable = Go4LunchStream.st


    }

    private void signOutFromUserFirebase() {
        if (UserManager.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnSuccessListener(this, this.updateUIAfterRestRequestsCompleted(SIGN_OUT_TASK));
        }
    }

    private OnSuccessListener<Void> updateUIAfterRestRequestsCompleted(final int origin) {
        return aVoid -> {
            switch (origin) {
                case SIGN_OUT_TASK:
                    finish();
                    break;
                default:
                    break;
            }
        };
    }

    private void showSnackBar (String message) {
        Snackbar.make(mDrawerLayout, message, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        // A°5 HANDLE BACK CLICK TO CLOSE MENU
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    private void configureUINavHeader() {
        if (UserManager.getCurrentUser() != null) {
            //RETURN LAYOUT
            View headerContainer = mNavigationView.getHeaderView(0);
            ImageView mPhotoHead = headerContainer.findViewById(R.id.header_photo);
            TextView mNameHead = headerContainer.findViewById(R.id.header_name);
            TextView mMailHead = headerContainer.findViewById(R.id.header_mail);
            //GET PHOTO IN FIREBASE
            if(UserManager.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(UserManager.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mPhotoHead);
            } else {
                mPhotoHead.setImageResource(R.drawable.no_pic);
            }

            //GET NAME
            String name = TextUtils.isEmpty(UserManager.getCurrentUser().getDisplayName()) ?
                    ("No Username") : UserManager.getCurrentUser().getDisplayName();
            //GET EMAIL
            String email = TextUtils.isEmpty(UserManager.getCurrentUser().getEmail()) ?
                    ("No Email Found") : UserManager.getCurrentUser().getEmail();
            //Update With data
            mNameHead.setText(name);
            mMailHead.setText(email);



        }
    }
    // CONNECTION ON FRAGMENTS WITH BUTTON
    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.map_button:
                        selectedFragment = new MapFragment();
                        break;

                    case R.id.list_button:
                        selectedFragment = new ListFragment();
                        break;

                    case R.id.coworkers_button:
                        selectedFragment = new CoworkersFragment();
                        break;

                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame_layout,
                            selectedFragment).commit();
                }
                return true;

            };

}