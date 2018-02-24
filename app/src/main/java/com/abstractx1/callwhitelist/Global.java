package com.abstractx1.callwhitelist;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by tfisher on 24/02/2018.
 */

public class Global {
    public static boolean canBlockPhoneCall(Context context) {
        int blockPhoneCallsPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.BLOCK_PHONE_CALLS);
        return blockPhoneCallsPermission == PackageManager.PERMISSION_GRANTED;
    }
}
