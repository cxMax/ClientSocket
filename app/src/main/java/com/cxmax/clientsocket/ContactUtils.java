package com.cxmax.clientsocket;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * ClientSocket.com.cxmax.clientsocket
 *
 * @ClassName: ContactUtils
 * @Description:
 * @Author: tanlin
 * @Date: 2019-08-31
 * @Version: 1.0
 */
public class ContactUtils {

    private static final String TAG = Constants.TAG + "/ContactUtils";

    public static List<ContactBean> getAllContacts(Context context) {
        List<ContactBean> list = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Uri contactsRow = ContactsContract.Contacts.CONTENT_URI;
        Uri data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = resolver.query(contactsRow, null, null, null, null);

        if (cursor == null) {
            return list;
        }
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            String number;
            Cursor item = resolver.query(data, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

            if (item == null) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            while (item.moveToNext()) {
                int index = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                sb.append(item.getString(index));
                sb.append(";");
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
            number = sb.toString();
            ContactBean info = new ContactBean(id, name, number);
            item.close();
            list.add(info);
        }

        cursor.close();
        return list;
    }
}
