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
import com.abstractx1.callwhitelist.MainActivity;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.List;

public class PhoneStateReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 0;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
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
                    telephonyService = (ITelephony) getITelephonyMethod.invoke(telephonyManager);
                    telephonyService.silenceRinger();
                    telephonyService.endCall();
                    MainActivity.log(String.format("endCall for incoming number: %s", incomingNumber));
                    sendNotification(context, incomingNumber);
                }
                else {
                    MainActivity.log(String.format("whitelisted contact for incoming number: %s", incomingNumber));
                }
            } catch (Exception e) {
                e.printStackTrace();
                MainActivity.log("FATAL ERROR: could not connect to telephony subsystem");
                MainActivity.log("Exception object: " + e);
            }
        }
    }

    private void sendNotification(Context context, String blockedPhoneNumber) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create the NotificationChannel, but only on API 26+ because
//            // the NotificationChannel class is new and not in the support library
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManagerCompat.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.createNotificationChannel(channel);
//        }


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "CHANNEL_ID")
                        .setSmallIcon(android.R.drawable.ic_menu_call)
                        .setContentTitle("CallWhiteList")
                        .setContentText(String.format("Blocked call %s", blockedPhoneNumber))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}