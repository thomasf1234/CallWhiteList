package com.abstractx1.callwhitelist.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.abstractx1.callwhitelist.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.add(R.id.fragment_container, settingsFragment, "HELLO");
        fragmentTransaction.commit();
    }

    public static void log(String message) {
        if (isInDebugMode()) {
            Log.v(getLogKey(), message);
        }
    }

    public static boolean isInDebugMode() {
        return false;
    }

    public static String getLogKey() {
        return "DEBUG";
    }
}
