package com.abstractx1.callwhitelist;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfisher on 24/02/2018.
 */

public class ContactUtils {
    private static final String SPACE = "\\s";
    public static List<String> getPhoneNumbers(Context context) {
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        List<String> phoneNumbers = new ArrayList<String>();

        while (phones.moveToNext())
        {
            String rawPhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phoneNumber = rawPhoneNumber.replaceAll(SPACE,"").replaceAll("\\+44","0");
            if (!phoneNumbers.contains(phoneNumber)) {
                phoneNumbers.add(phoneNumber);
            }
        }
        phones.close();

        return  phoneNumbers;
    }
}
