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
import com.example.go4lunch.utils.AlertReceiver;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.alarmOn)
    Button mAlarmOn;
    @BindView(R.id.alarmOff)
    Button mAlarmOff;
    @BindView(R.id.settings_activity_layout)
    RelativeLayout mRelativeLayout;

    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        this.alarmOn();
        this.alarmOff();

        if(mAlarmOn.isEnabled() && mAlarmOff.isEnabled()) {
            mAlarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_white_100));

        } else if (!mAlarmOn.isEnabled() && !mAlarmOff.isEnabled()) {
            mAlarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
    }

    private void alarmOn() {
        mAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSet();
                showSnackBar(getString(R.string.activation_alarm));

                if(mAlarmOn.isEnabled()) {
                    mAlarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_white_100));
                    mAlarmOff.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                } else if (!mAlarmOn.isEnabled()) {
                    mAlarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    mAlarmOff.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_white_100));
                }

                SharedPreferences sharedPreferences = PreferenceManager.
                        getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("alarmOn", mAlarmOn.isEnabled());
                editor.apply();
            }
        });
    }

    private void alarmOff() {
        mAlarmOff.setOnClickListener( v -> {

            if(mAlarmOff.isEnabled()) {
                cancelAlarm();
                //mAlarmOff.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                //mAlarmOn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mAlarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                mAlarmOff.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_white_100));
                showSnackBar(getString(R.string.desactivation_alarm));
            } else if (!mAlarmOff.isEnabled()) {
                onTimeSet();
                //mAlarmOff.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                //mAlarmOn.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                mAlarmOff.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                mAlarmOn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.quantum_white_100));
                showSnackBar(getString(R.string.activation_alarm));

                SharedPreferences sharedPreferences= androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("alarmOff", mAlarmOff.isEnabled());
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

    private void showSnackBar (String message) {
        Snackbar.make(mRelativeLayout, message, Snackbar.LENGTH_SHORT).show();
    }



}
