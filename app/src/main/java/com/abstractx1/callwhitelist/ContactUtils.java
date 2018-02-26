package com.abstractx1.callwhitelist;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneNumberUtils;

import com.abstractx1.callwhitelist.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfisher on 24/02/2018.
 */

public class ContactUtils {
    public static List<Contact> getContacts(Context context) {
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        List<Contact> contacts = new ArrayList<Contact>();

        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String rawPhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phoneNumber = PhoneNumberUtils.normalizeNumber(rawPhoneNumber);
            Contact contact = new Contact(name, phoneNumber);
            contacts.add(contact);
        }
        phones.close();

        return contacts;
    }
}
