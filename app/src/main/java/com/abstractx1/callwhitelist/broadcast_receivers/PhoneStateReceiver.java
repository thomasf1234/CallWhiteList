package com.abstractx1.callwhitelist.broadcast_receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

import com.abstractx1.callwhitelist.ContactUtils;
import com.abstractx1.callwhitelist.Global;
import com.abstractx1.callwhitelist.MainActivity;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PhoneStateReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        String intentAction = intent.getAction();
        if (intentAction.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            Bundle bundle = intent.getExtras();

            if(bundle != null) {
                try {
                    // Java reflection to gain access to TelephonyManager's
                    // ITelephony getter
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
                    Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
                    getITelephonyMethod.setAccessible(true);
                    ITelephony telephonyService = (ITelephony) getITelephonyMethod.invoke(telephonyManager);
                    Bundle _bundle = intent.getExtras();
                    String incomingNumber = _bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    MainActivity.log(String.format("Incoming number: %s", incomingNumber));

                    List<String> phoneNumbers = ContactUtils.getPhoneNumbers(context);

                    if (!phoneNumbers.contains(incomingNumber)) {
                        if (Global.canBlockPhoneCall(context)) {
                            // Permission is granted
                            telephonyService = (ITelephony) getITelephonyMethod.invoke(telephonyManager);
                            telephonyService.silenceRinger();
                            telephonyService.endCall();
                            MainActivity.log(String.format("endCall for incoming number: '%s'", incomingNumber));
                            sendNotification(context, incomingNumber);
                        }
                        else {
                            MainActivity.log(String.format("require permission com.abstractx1.callwhitelist.permission.BLOCK_PHONE_CALLS to block call"));
                        }
                    }
                    else {
                        MainActivity.log(String.format("whitelisted contact for incoming number: '%s'", incomingNumber));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.log("FATAL ERROR: could not connect to telephony subsystem");
                    MainActivity.log("Exception object: " + e);
                }
            }
        }
    }

    private void sendNotification(Context context, String blockedPhoneNumber) {
        Date now = new Date();
        String nowString = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(now);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "CHANNEL_ID")
                        .setSmallIcon(android.R.drawable.ic_menu_call)
                        .setContentTitle("CallWhiteList")
                        .setContentText(String.format("Blocked call %s at %s", blockedPhoneNumber, nowString))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
