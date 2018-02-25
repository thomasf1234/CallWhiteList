package com.abstractx1.callwhitelist;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.abstractx1.callwhitelist.activities.MainActivity;

/**
 * Created by tfisher on 24/02/2018.
 */

public class Global {
    public static final String USER_SETTINGS_KEY = "user_settings";
    public static final String ONLY_ALLOW_CONTACTS_KEY = "only_allow_contacts";
    public static final String BLOCK_HIDDEN_NUMBERS_KEY = "block_hidden_numbers";
    public static final String ENABLE_BLACKLIST_KEY = "enable_blacklist";

    public static boolean hasPermissionBlockPhoneCall(Context context) {
        boolean hasPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
        return hasPermission;
    }

    public static boolean getToggle(Context context, String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(USER_SETTINGS_KEY, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(key))
        {
            return sharedpreferences.getBoolean(key, false);
        }
        else
        {
            return false;
        }
    }

    public static void setToggle(Context context, String key, boolean value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(USER_SETTINGS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        MainActivity.log(String.format("Setting %s value to %s", key, value));
        editor.commit();
    }
}

