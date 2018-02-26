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
        Cursor contactsCursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null,null, null);
        List<Contact> contacts = new ArrayList<Contact>();

        while (contactsCursor.moveToNext())
        {
            String id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            List<String> phoneNumbers = getPhoneNumbers(context, id);

            Contact contact = new Contact(name, phoneNumbers);
            contacts.add(contact);
        }
        contactsCursor.close();

        return contacts;
    }

    public static List<String> getPhoneNumbers(Context context, String contactId) {
        Cursor phonesCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,null, null);
        List<String> phoneNumbers = new ArrayList<String>();

        while (phonesCursor.moveToNext())
        {

            String rawPhoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phoneNumber = PhoneNumberUtils.normalizeNumber(rawPhoneNumber);
            phoneNumbers.add(phoneNumber);
        }
        phonesCursor.close();

        return phoneNumbers;
    }

    public static String replace44(String phoneNumber) {
        return phoneNumber.replace("+44", "0");
    }
}
