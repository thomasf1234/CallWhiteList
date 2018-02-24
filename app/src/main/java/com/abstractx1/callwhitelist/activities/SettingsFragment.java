package com.abstractx1.callwhitelist.activities;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.RequiresApi;

import com.abstractx1.callwhitelist.Global;
import com.abstractx1.callwhitelist.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class SettingsFragment extends PreferenceFragment  {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        SwitchPreference enableWhitelistPreference = (SwitchPreference) findPreference("enable_whitelist_preference");
        enableWhitelistPreference.setChecked(Global.getEnableWhitelistToggle(getContext()));
        enableWhitelistPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onPreferenceChange(Preference arg0, Object enabledObject) {
                boolean enabled = (Boolean) enabledObject;
                Global.setEnableWhitelistToggle(getContext(), enabled);
                return true;
            }
        });
    }
}