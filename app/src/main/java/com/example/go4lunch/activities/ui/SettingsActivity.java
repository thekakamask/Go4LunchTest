package com.example.go4lunch.activities.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityRestaurantBinding;
import com.example.go4lunch.databinding.SettingActivityBinding;
import com.example.go4lunch.utils.AlertReceiver;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

// 1µ = CHANGEMENTS DU BIND DES VIEWS DE L'XML ; AVANT UTILISATION DE BUTTERKNIFE ET MAINTENANT
// UTILISATION DE L'HERITAGE DE LA CLASSE BASEACTIVITY QUI ELLE S'OCCUPE DE BINDER LES VIEWS
// CHAQUE CHANGEMENT EST INDIQUE PAR 1µ AU DEBUT

// 1µ : AVANT CHANGEMENT : extends AppCompatActivity et @BindView avec le relative layout et les deux boutons du XML
// APRES CHANGEMENT : extends BaseActivity<SettingActivity> (le bind du xml activity_main)
public class SettingsActivity extends BaseActivity<SettingActivityBinding> {

    /*@BindView(R.id.alarmOn)
    Button mAlarmOn;
    @BindView(R.id.alarmOff)
    Button mAlarmOff;
    @BindView(R.id.settings_activity_layout)
    RelativeLayout mRelativeLayout;*/

    private Calendar c;

    //1µ : DEBUT AJOUT (inflate du layout de activity_restaurant)
    @Override
    SettingActivityBinding getViewBinding() {
        return SettingActivityBinding.inflate(getLayoutInflater());
    }
    //1µ :FIN AJOUT

    // 1µ : AVANT CHANGEMENT : protected void onCreate (au lieu de public) et setContentView(R.layout.activity_restaurant)
    // et ButterKnife.bind(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.setting_activity);
        ButterKnife.bind(this);*/

        this.alarmOn();
        this.alarmOff();

        //1µ : remplacement de mAlarmOn (qui etait lié avec @BindView(R.id.alarmOn) Button mAlarmOn;
        // par binding.alarmOn (star_btn (id de l'xml) sans le _)
        // et aussi avec @BindView(R.id.alarmOff)  Button mAlarmOff;
        if(binding.alarmOn.isEnabled() && binding.alarmOff.isEnabled()) {
            binding.alarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_white_100));

        } else if (!binding.alarmOn.isEnabled() && !binding.alarmOff.isEnabled()) {
            binding.alarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
    }

    //1µ : remplacement de mAlarmOn (qui etait lié avec @BindView(R.id.alarmOn) Button mAlarmOn;
    // par binding.alarmOn (star_btn (id de l'xml) sans le _)
    // et aussi avec @BindView(R.id.alarmOff)  Button mAlarmOff;
    private void alarmOn() {
        binding.alarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSet();
                showSnackBar(getString(R.string.activation_alarm));

                if(binding.alarmOn.isEnabled()) {
                    binding.alarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_white_100));
                    binding.alarmOff.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                } else if (!binding.alarmOn.isEnabled()) {
                    binding.alarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    binding.alarmOff.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_white_100));
                }

                SharedPreferences sharedPreferences = PreferenceManager.
                        getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("alarmOn", binding.alarmOn.isEnabled());
                editor.apply();
            }
        });
    }

    //1µ : remplacement de mAlarmOn (qui etait lié avec @BindView(R.id.alarmOn) Button mAlarmOn;
    // par binding.alarmOn (star_btn (id de l'xml) sans le _)
    // et aussi avec @BindView(R.id.alarmOff)  Button mAlarmOff;
    private void alarmOff() {
        binding.alarmOff.setOnClickListener( v -> {

            if(binding.alarmOff.isEnabled()) {
                cancelAlarm();
                //mAlarmOff.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                //mAlarmOn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.alarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                binding.alarmOff.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_white_100));
                showSnackBar(getString(R.string.desactivation_alarm));
            } else if (!binding.alarmOff.isEnabled()) {
                onTimeSet();
                //mAlarmOff.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                //mAlarmOn.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                binding.alarmOff.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                binding.alarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.quantum_white_100));
                showSnackBar(getString(R.string.activation_alarm));

                SharedPreferences sharedPreferences= androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("alarmOff", binding.alarmOff.isEnabled());
                editor.apply();

            }
        });
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (this,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1, intent, 0);
        Objects.requireNonNull(alarmManager).cancel(pendingIntent);

    }

    public void onTimeSet() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        startAlarm(c);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1, intent, 0);
        Objects.requireNonNull(alarmManager).cancel(pendingIntent);
    }

    //1µ : remplacement de mRelativeLayout (qui etait lié avec @BindView(R.id.settings_activity_layout) RelativeLayout mRelativeLayout;
    // par binding.settingsActivityLayout (settings_activity_layout (id de l'xml) sans le _)
    private void showSnackBar (String message) {
        Snackbar.make(binding.settingsActivityLayout, message, Snackbar.LENGTH_SHORT).show();
    }



}
