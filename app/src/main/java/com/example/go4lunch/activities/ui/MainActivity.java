package com.example.go4lunch.activities.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.fragments.chat.ChatFragment;
import com.example.go4lunch.activities.ui.fragments.coworkers.CoworkersFragment;
import com.example.go4lunch.activities.ui.fragments.list.ListFragment;
import com.example.go4lunch.activities.ui.fragments.map.MapFragment;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetail;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repository.StreamRepository;
import com.example.go4lunch.utils.AlertReceiver;
import com.example.go4lunch.viewModels.UserManager;
import com.example.go4lunch.viewModels.UserViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.Calendar;
import java.util.Objects;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;


// 1µ = CHANGEMENTS DU BIND DES VIEWS DE L'XML ; AVANT UTILISATION DE BUTTERKNIFE ET MAINTENANT
// UTILISATION DE L'HERITAGE DE LA CLASSE BASEACTIVITY QUI ELLE S'OCCUPE DE BINDER LES VIEWS
// CHAQUE CHANGEMENT EST INDIQUE PAR 1µ AU DEBUT

// 1µ : AVANT CHANGEMENT : extends AppCompatActivity et @BindView avec le layout et la toolbar et les deux navigationsViews
// APRES CHANGEMENT : extends BaseActivity<ActivityMainBinding> (le bind du xml activity_main)
public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {

    /*@BindView(R.id.main_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_bottom)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.main_activity_nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.main_activity_drawer_layout)
    DrawerLayout mDrawerLayout;*/



    private Disposable mDisposable;
    private String idResto;
    private PlaceDetail detail;
    private final UserManager userManager = UserManager.getInstance();
    private UserViewModel userViewModel;

    //private DrawerLayout drawerLayout; USE FINDVIEW BY ID METHOD
    //private NavigationView navigationView; USE FINDVIEW BY ID METHOD
    /*  private Disposable mDisposable;
    private PlaceDetail detail;
    private String idResto;   */

    //1µ : DEBUT AJOUT (inflate du layout de activity_main)
    @Override
    ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    //1µ :FIN AJOUT

    // 1µ : AVANT CHANGEMENT : protected void onCreate (au lieu de public) et setContentView(R.layout.activity_main)
    // et ButterKnife.bind(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);

        //INIT VIEWMODEL WITH PROVIDERS
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // A°6 CONFIGURE ALL VIEWS
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureUINavHeader();
        this.onTimeSet();

        //FOR BOTTOM NAVIGATION VIEW
        //1µ : remplacement de mBottonNavigationView (qui etait lié avec @BindView(R.id.navigation_bottom) BottomNavigationView mBottomNavigationView;
        // par binding.navigationBottom (navigation_bottom (id de l'xml) sans le _)
        binding.navigationBottom.setOnNavigationItemSelectedListener(navigationListener);


        //MAPFRAGMENT CONNECTION WITH ACTIVITY
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame_layout,
                new MapFragment()).commit();

        //ACTION BAR TITLE
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_bar);
        }

        //FOR ALARM OFF

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.getBoolean("alarmOff", false);
        sharedPreferences.getBoolean("alarmOn", false);
        Log.d("TestAlarmOff", String.valueOf(sharedPreferences.getBoolean("AlarmOff", false)));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // A°1 CONFIGURE TOOLBAR
    //1µ : remplacement de mToolbar (qui etait lié avec @BindView(R.id.main_activity_toolbar) Toolbar mToolbar;
    // par binding.mainActivityToolbar  (main_activity_toolbar (id de l'xml) mais sans les _)
    private void configureToolbar() {
        setSupportActionBar(binding.mainActivityToolbar);
    }

    // A°2 CONFIGURE DRAWER LAYOUT
    //1µ : remplacement de mDrawerLayout (qui etait lié avec @BindView(R.id.main_activity_drawer_layout) DrawerLayout mDrawerLayout;
    // par binding.mainActivityDrawerLayout  (main_activity_drawer_layout (id de l'xml) mais sans les _)
    // pareil pour la toolbar (voir ligne 138)
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.mainActivityDrawerLayout, binding.mainActivityToolbar,
                R.string.main_activity_navigation_drawer_open,
                R.string.main_activity_navigation_drawer_close);
        binding.mainActivityDrawerLayout.addDrawerListener(toggle);
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
    //1µ : remplacement de mNavigationView (qui etait lié avec @BindView(R.id.main_activity_nav_view) NavigationView mNavigationView;
    // par binding.mainActivityNavView (main_activity_nav_view (id de l'xml) sans le _)
    private void configureNavigationView() {
        binding.mainActivityNavView.setNavigationItemSelectedListener(this);
    }

    //USE FINDVIEW BY ID METHOD
    /*private void configureNavigationView() {
        this.navigationView = findViewById(R.id.main_activity_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }*/

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // A°4 HANDLE NAVIGATION ITEM CLICK
        int id = item.getItemId();
        switch(id) {
            case R.id.lunch_menu_drawer :
                if(userManager.getCurrentUser() != null) {
                    UserManager.getInstance().getUserData(userManager.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
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
                showSnackBar(getString(R.string.log_out));
                break;
        }

        //1µ : remplacement de mDrawerLayout (qui etait lié avec @BindView(R.id.main_activity_drawer_layout) DrawerLayout mDrawerLayout;
        // par binding.mainActivityDrawerLayout  (main_activity_drawer_layout (id de l'xml) mais sans les _)
        this.binding.mainActivityDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void userResto(User users) {
        idResto = users.getIdOfPlace();
        executeHttpRequestWithRetrofit();
    }

    private void executeHttpRequestWithRetrofit() {
       this.mDisposable = StreamRepository.streamFetchDetails(idResto)
               .subscribeWith(new DisposableObserver<PlaceDetail>() {
                   @Override
                   public void onNext(PlaceDetail placeDetail) {
                       detail = placeDetail;
                       startForLunch();
                   }

                   @Override
                   public void onComplete() {
                       if (idResto != null) {
                           Log.d("your lunch request", "your lunch" + detail.getResult());
                       }
                   }

                   @Override
                   public void onError(Throwable e) {
                       Log.d("onErrorYourLunch", Log.getStackTraceString(e));
                   }
               });


    }

    public void startForLunch() {
        Intent intent = new Intent(this, RestaurantActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("placeDetailsResult", detail.getResult());
        intent.putExtras(bundle);
        Log.d(TAG, "startForLunch: "+ detail.getResult().getName());
        this.startActivity(intent);

    }

    private void signOutFromUserFirebase() {
        if (userManager.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnSuccessListener(this, this.updateUIAfterRestRequestsCompleted());
        }
    }

    private OnSuccessListener<Void> updateUIAfterRestRequestsCompleted() {
        return aVoid -> finish();
    }

    //1µ : remplacement de mDrawerLayout (qui etait lié avec @BindView(R.id.main_activity_drawer_layout) DrawerLayout mDrawerLayout;
    // par binding.mainActivityDrawerLayout  (main_activity_drawer_layout (id de l'xml) mais sans les _)
    private void showSnackBar (String message) {
        Snackbar.make(binding.mainActivityDrawerLayout, message, Snackbar.LENGTH_SHORT).show();
    }


    //1µ : remplacement de mDrawerLayout (qui etait lié avec @BindView(R.id.main_activity_drawer_layout) DrawerLayout mDrawerLayout;
    // par binding.mainActivityDrawerLayout  (main_activity_drawer_layout (id de l'xml) mais sans les _)
    @Override
    public void onBackPressed() {
        // A°5 HANDLE BACK CLICK TO CLOSE MENU
        if (this.binding.mainActivityDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.binding.mainActivityDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    //1µ : remplacement de mNavigationView (qui etait lié avec @BindView(R.id.main_activity_nav_view) NavigationView mNavigationView;
    //  par binding.mainActivityNavView (main_activity_nav_view (id de l'xml) sans le _)
    private void configureUINavHeader() {
        if (userManager.getCurrentUser() != null) {
            //RETURN LAYOUT
            View headerContainer = binding.mainActivityNavView.getHeaderView(0);
            ImageView mPhotoHead = headerContainer.findViewById(R.id.header_photo);
            TextView mNameHead = headerContainer.findViewById(R.id.header_name);
            TextView mMailHead = headerContainer.findViewById(R.id.header_mail);
            //GET PHOTO IN FIREBASE
            if(userManager.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(userManager.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mPhotoHead);
            } else {
                mPhotoHead.setImageResource(R.drawable.no_pic);
            }

            //GET NAME
            String name = TextUtils.isEmpty(userManager.getCurrentUser().getDisplayName()) ?
                    ("No Username") : userManager.getCurrentUser().getDisplayName();
            //GET EMAIL
            String email = TextUtils.isEmpty(userManager.getCurrentUser().getEmail()) ?
                    ("No Email Found") : userManager.getCurrentUser().getEmail();
            //Update With data
            mNameHead.setText(name);
            mMailHead.setText(email);



        }
    }
    // CONNECTION ON FRAGMENTS WITH BUTTON
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
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

                    case R.id.chat_button:
                        selectedFragment = new ChatFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame_layout,
                            selectedFragment).commit();
                }
                return true;

            };


    //SETTING HOUR OF NOTIF
    public void onTimeSet() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        startAlarm(c);
    }

    //FOR NOTIF
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (this, AlertReceiver.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1, intent,0);

        if(c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

}