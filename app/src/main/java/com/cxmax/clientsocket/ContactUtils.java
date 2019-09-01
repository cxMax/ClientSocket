package com.cxmax.clientsocket;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
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
            Cursor phoneCurse = resolver.query(data, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

            if (phoneCurse == null) {
                continue;
            }
            List<ContactBean.Phone> phones = new ArrayList<>();

            while (phoneCurse.moveToNext()) {
                int numberIndex = phoneCurse.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int typeIndex = phoneCurse.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                ContactBean.Phone phone = new ContactBean.Phone();
                phone.number = phoneCurse.getString(numberIndex);
                phone.type = phoneCurse.getInt(typeIndex);
                phones.add(phone);
            }

            ContactBean info = new ContactBean(id, name, phones);
            phoneCurse.close();
            list.add(info);
        }

        cursor.close();
        return list;
    }

    public static void deleteContact(Context context, String id) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=" + id, null)
                .build());
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=" + id, null)
                .build());
        handleApplyBatch(context, ContactsContract.AUTHORITY, ops, "deleteContact");
    }

    /**
     * 批量删除, 215条数据，测试机删除时间为 5s 左右
     * @param context ctx: Activity/Application
     * @param contacts {@link ContactBean}
     */
    public static void deleteContacts(Context context, List<ContactBean> contacts) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        // list 最大 500, 因此将batchSize设置为 200 < 250 = 500 / (1 + 1)
        int batchSize = 200;
        int i = 0;
        for (ContactBean contact : contacts) {
            String id = contact.id;
            // 删除 RowContact
            ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=" + id, null)
                    .build());
            // 删除关联的data，phone/email
            ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=" + id, null)
                    .build());
            i++;
            if (i % batchSize == 0) {
                handleApplyBatch(context, ContactsContract.AUTHORITY, ops, "deleteContacts");
                ops.clear();
            }
        }
        handleApplyBatch(context, ContactsContract.AUTHORITY, ops, "deleteContacts");
    }

    /**
     * 批量插入, 215条数据，测试机插入时间为 14s 左右
     * @param context ctx: Activity/Application
     * @param contacts {@link ContactBean}
     */
    public static void insertContact(Context context, List<ContactBean> contacts) {
        int i = 0;
        // 最大 500，考录到一个账户的手机号可能有3个，因此设置为 100 <= 100 = 500 / (1 + 1 + 3)，
        int batchSize = 100;
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        for (ContactBean contact : contacts) {
            // 关键代码
            int rawContactInsertIndex = ops.size();
            // 先插入 RowContact, 生成contact_id, ref: https://m.ydaobo.com/wenzhang/63413.html
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            // `withValueBackReference` 使用上一条记录生成的id，继续插入data表
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.name)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.name)
                    .withYieldAllowed(true)
                    .build());
            List<ContactBean.Phone> phones = contact.phones;
            // 插入所有类型的手机号
            for (ContactBean.Phone p : phones) {
                ops.add(buildOp(ContactsContract.Data.CONTENT_URI, p, rawContactInsertIndex));
            }
            ++i;
            if (i % batchSize == 0) {
                handleApplyBatch(context, ContactsContract.AUTHORITY, ops, "insertContact");
                Log.i(TAG, "insertContact: end batchSize:" + batchSize);
                ops.clear();
            }
        }
        handleApplyBatch(context, ContactsContract.AUTHORITY, ops, "insertContact");
    }

    /**
     * 需要一行一行插入，否则在applyBatch只能讲第一条数据插入，后续的都为空，理论上应该支持才对
     * @param context ctx: Activity/Application
     * @param contact {@link ContactBean}
     */
    public static void insertContact(Context context, ContactBean contact) {
        int rawContactInsertIndex = 0;
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        // 先插入 RowContact, 生成contact_id, ref: https://m.ydaobo.com/wenzhang/63413.html
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // `withValueBackReference` 使用上一条记录生成的id，继续插入data表
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.name)
                // .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.name)
                .withYieldAllowed(true)
                .build());
        List<ContactBean.Phone> phones = contact.phones;
        for (ContactBean.Phone p : phones) {
            ops.add(buildOp(ContactsContract.Data.CONTENT_URI, p, rawContactInsertIndex));
        }
        handleApplyBatch(context, ContactsContract.AUTHORITY, ops, "insertContact");
    }

    /**
     * 生成插入操作语句，因为源数据中一个联系人可能有多个电话号码，需要都插入新手机
     * @param uri content://com.android.contacts/data
     * @param phone type + number
     */
    private static ContentProviderOperation buildOp(Uri uri, ContactBean.Phone phone, int previousResult) {
        return ContentProviderOperation.newInsert(uri)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, previousResult)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phone.type)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.number)
                .withYieldAllowed(true)
                .build();
    }

    private static void handleApplyBatch(Context context, String authority, ArrayList<ContentProviderOperation> ops, String opType) {
        try {
            context.getContentResolver().applyBatch(authority, ops);
        } catch (OperationApplicationException | RemoteException e) {
            Log.e(TAG, "handleApplyBatch " + opType + " exception:" + e);
        }
    }
}
