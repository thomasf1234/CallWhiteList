package com.abstractx1.callwhitelist.singletons;

import android.content.Context;
import android.util.Log;

import com.abstractx1.callwhitelist.Global;

/**
 * Created by tfisher on 26/02/2018.
 */

public class SLogger {
    private static final SLogger ourInstance = new SLogger();

    public static SLogger getInstance() {
        return ourInstance;
    }

    private SLogger() {
    }

    public void logDebug(Context context, String message) {
        if (Global.isInDebugMode(context)) {
            Log.d(getLogKey(), message);
        }
    }

    private String getLogKey() {
        return getClass().getName();
    }
}
