package com.abstractx1.callwhitelist.broadcast_receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

import com.abstractx1.callwhitelist.utils.ContactUtils;
import com.abstractx1.callwhitelist.Global;
import com.abstractx1.callwhitelist.R;
import com.abstractx1.callwhitelist.models.Contact;
import com.abstractx1.callwhitelist.singletons.SLogger;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneStateReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED))
        {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                String extraState = bundle.getString(TelephonyManager.EXTRA_STATE);
                
                if (extraState != null &&
                        (extraState.equals(TelephonyManager.EXTRA_STATE_RINGING) || extraState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                    try {

                        if (Global.hasPermissionBlockPhoneCall(context)) {
                            // Permission is granted

                            boolean blockCall = true;

                            // Java reflection to gain access to TelephonyManager's
                            // ITelephony getter
                            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                            Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
                            Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
                            getITelephonyMethod.setAccessible(true);
                            ITelephony telephonyService = (ITelephony) getITelephonyMethod.invoke(telephonyManager);
                            String incomingNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                            SLogger.getInstance().logDebug(context, String.format("Incoming number: %s", incomingNumber));

                            if (incomingNumber == null || incomingNumber.length() == 0) {
                                SLogger.getInstance().logDebug(context, "Incoming number is a hidden number");
                                if (Global.getToggle(context, Global.ONLY_ALLOW_CONTACTS_KEY) || Global.getToggle(context, Global.BLOCK_HIDDEN_NUMBERS_KEY)) {
                                    SLogger.getInstance().logDebug(context, "Blocking hidden number");
                                } else
                                {
                                    blockCall = false;
                                }
                            } else {
                                SLogger.getInstance().logDebug(context,"Incoming number is not a hidden number");
                                incomingNumber = ContactUtils.replace44(incomingNumber);

                                SLogger.getInstance().logDebug(context,"Reading contacts");
                                List<Contact> contacts = ContactUtils.getContacts(context);

                                for (Contact contact : contacts) {
                                    SLogger.getInstance().logDebug(context, String.format("Found contact name '%s' numbers '%s'", contact.getName(), contact.getPhoneNumbers()));
                                }

                                if (Global.getToggle(context, Global.ONLY_ALLOW_CONTACTS_KEY))
                                {
                                    for (Contact contact : contacts) {
                                        if (!contact.isBlacklistEntry() && contact.hasPhoneNumber()) {
                                            for (String phoneNumber : contact.getPhoneNumbers()) {
                                                SLogger.getInstance().logDebug(context, String.format("Checking contact name '%s' number '%s'", contact.getName(), phoneNumber));
                                                if (incomingNumber.equals(ContactUtils.replace44(phoneNumber))) {
                                                    blockCall = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                else if (Global.getToggle(context, Global.ENABLE_BLACKLIST_KEY))
                                {
                                    boolean isBlacklisted = false;

                                    for (Contact contact : contacts) {
                                        if (contact.isBlacklistEntry()) {
                                            Pattern blacklistPattern = contact.blackListPattern();
                                            SLogger.getInstance().logDebug(context, String.format("Checking blacklist entry '%s'", blacklistPattern.toString()));
                                            Matcher matcher = blacklistPattern.matcher(incomingNumber);
                                            if (matcher.find( )) {
                                                SLogger.getInstance().logDebug(context, String.format("Entry is a match for %s, blocking call", blacklistPattern.toString()));
                                                isBlacklisted = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!isBlacklisted) {
                                        SLogger.getInstance().logDebug(context, "No matching blacklist entry so allowing call");
                                        blockCall = false;
                                    }
                                }
                                else {
                                    SLogger.getInstance().logDebug(context, "No toggles enabled so allowing call");
                                    blockCall = false;
                                }
                            }


                            if (blockCall == true) {
                                telephonyService = (ITelephony) getITelephonyMethod.invoke(telephonyManager);
                                telephonyService.silenceRinger();
                                telephonyService.endCall();
                                SLogger.getInstance().logDebug(context, String.format("endCall for incoming number: '%s'", incomingNumber));
                                sendNotification(context, incomingNumber);
                            }
                        } else {
                            SLogger.getInstance().logDebug(context, String.format("require permissions to continue"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        SLogger.getInstance().logDebug(context, "Exception object: " + e);
                    }
                }
            }
        }


    }

    private void sendNotification(Context context, String blockedPhoneNumber) {
        Resources resources = context.getResources();
        Date now = new Date();
        String nowString = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(now);
        String message;

        if (blockedPhoneNumber == null || blockedPhoneNumber.length() == 0) {
            message = String.format("Blocked call number witheld at %s", nowString);
        } else
        {
            message = String.format("Blocked call %s at %s", blockedPhoneNumber, nowString);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "CHANNEL_ID")
                        .setSmallIcon(android.R.drawable.ic_menu_call)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
                        .setContentTitle("CallWhiteList")
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
