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
    public static final String ENABLE_WHITELIST_KEY = "enable_whitelist";

    public static boolean hasPermissionBlockPhoneCall(Context context) {
        boolean hasPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
        return hasPermission;
    }

    public static boolean getEnableWhitelistToggle(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(USER_SETTINGS_KEY, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(ENABLE_WHITELIST_KEY))
        {
            return sharedpreferences.getBoolean(ENABLE_WHITELIST_KEY, false);
        }
        else
        {
            return false;
        }
    }

    public static void setEnableWhitelistToggle(Context context, boolean value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(USER_SETTINGS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(ENABLE_WHITELIST_KEY, value);
        MainActivity.log(String.format("Setting %s value to %s", ENABLE_WHITELIST_KEY, value));
        editor.commit();
    }
}

