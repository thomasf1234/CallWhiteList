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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        SwitchPreference onlyAllowContactsPreference = (SwitchPreference) findPreference("only_allow_contacts_preference");
        onlyAllowContactsPreference.setChecked(Global.getToggle(getActivity().getApplicationContext(), Global.ONLY_ALLOW_CONTACTS_KEY));
        onlyAllowContactsPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onPreferenceChange(Preference arg0, Object enabledObject) {
                boolean enabled = (Boolean) enabledObject;
                Global.setToggle(getActivity().getApplicationContext(), Global.ONLY_ALLOW_CONTACTS_KEY, enabled);
                return true;
            }
        });

        SwitchPreference blockHiddenNumbersPreference = (SwitchPreference) findPreference("block_hidden_numbers_preference");
        blockHiddenNumbersPreference.setChecked(Global.getToggle(getActivity().getApplicationContext(), Global.BLOCK_HIDDEN_NUMBERS_KEY));
        blockHiddenNumbersPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onPreferenceChange(Preference arg0, Object enabledObject) {
                boolean enabled = (Boolean) enabledObject;
                Global.setToggle(getActivity().getApplicationContext(), Global.BLOCK_HIDDEN_NUMBERS_KEY, enabled);
                return true;
            }
        });

        SwitchPreference enableBlacklistPreference = (SwitchPreference) findPreference("enable_blacklist_preference");
        enableBlacklistPreference.setChecked(Global.getToggle(getActivity().getApplicationContext(), Global.ENABLE_BLACKLIST_KEY));
        enableBlacklistPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onPreferenceChange(Preference arg0, Object enabledObject) {
                boolean enabled = (Boolean) enabledObject;
                Global.setToggle(getActivity().getApplicationContext(), Global.ENABLE_BLACKLIST_KEY, enabled);
                return true;
            }
        });
    }
}