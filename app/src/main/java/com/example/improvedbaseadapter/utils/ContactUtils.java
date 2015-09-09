
package com.example.improvedbaseadapter.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.improvedbaseadapter.BuildConfig;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

/**
 * 通讯录读取记录功能类。
 * 
 * @author davidleen29
 */
public class ContactUtils {

    private static String TAG = "ContactUtils";

    private static String getFormalPhoneString(String mobile) {
        return mobile.replace("-", "").replace("+86", "").replace(" ", "").replace("(", "")
                .replace(")", "").trim();
    }

    /**
     * 判断某字符是否是英文字符
     * 
     * @param c
     * @return
     */
    public static boolean isAlpha(char c) {
        // A~Z 65-90 a~z 97~122
        if ((c >= 65 && c < 65 + 26) || (c >= 97 && c < 97 + 26)) {
            return true;
        }
        return false;

    }

    public static boolean isContactPermissionGranted(Context context)
    {
        int permission = context.checkPermission(android.Manifest.permission.READ_CONTACTS,
                Binder.getCallingPid(), Binder.getCallingUid());
        if (BuildConfig.DEBUG)
            Log.d(TAG, "permission:" + (permission == PackageManager.PERMISSION_GRANTED));
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 读取通讯录记录。
     * 
     * @return
     * @throws Exception
     */
    public static List<Contact> loadContactData(Context context) throws Exception
    {
        if (isContactPermissionGranted(context)) {
            // RecentDataUtils<Bean_Contact> recentDataUtils = new
            // RecentDataUtils<Bean_Contact>(CacheDataFileName,
            // RecentDataUtils.UN_LIMITED, CacheDataValidTime);

            List<Contact> list = new ArrayList<Contact>();

            // list.addAll( recentDataUtils.readRecentData(context));
            if (list.size() <= 0) {

                ContentResolver contentResolver = context.getContentResolver(); //
                Cursor phonecursor = null;

                // // 加上版本判断方式 不一定好用 sdk 要求是 11版本以上 但是 在部分8版本 也提供了这个字段。
                // phonecursor =
                // contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                // null, null, null,
                // Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                // ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY :
                // null);

                // 采用异常处理
                try {
                    phonecursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                            ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
                } catch (Throwable t) {
                    phonecursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                            null);
                    t.printStackTrace();

                }
                if (phonecursor != null)
                    // L.s(TAG, "phonecursor:" + phonecursor.getCount());
                    while (phonecursor.moveToNext()) {

                        Contact contact = new Contact();

                        String strMobile = phonecursor.getString(phonecursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.mobile = getFormalPhoneString(strMobile);

                        if (PhoneNumberUtils.isGlobalPhoneNumber(contact.mobile)) {// 只是符合手机号的电话号码才加入列表中
                            // 去掉名字中可能出现的空格
                            contact.name = phonecursor
                                    .getString(
                                            phonecursor
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                                    .replace(" ",
                                            "");

                            int pinyinIndex = phonecursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);

                            String pinyin = pinyinIndex > -1 ? phonecursor.getString(pinyinIndex)
                                    : null;
                            // L.e(TAG, contact.name +
                            // phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)));
                            contact.firstPinyin = pinyin != null && pinyin.length() > 0 ? pinyin
                                    .toUpperCase().charAt(0) : '#';
                            if (!isAlpha(contact.firstPinyin)) {

                                contact.firstPinyin = '#';
                            }
                            if (BuildConfig.DEBUG)
                                Log.d(TAG, "contact.firstPinyin:" + contact.firstPinyin);
                            Long photoid = phonecursor
                                    .getLong(phonecursor
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
                            Long contactid = phonecursor
                                    .getLong(phonecursor
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                            contact.contactId = contactid;
                            contact.hasPhoto = photoid > 0;
                            list.add(contact);
                        }
                    }
                try {
                    phonecursor.close();
                } catch (Throwable t) {
                }
                // 读取sim卡
                Uri uri = Uri.parse("content://icc/adn");
                try {
                    Cursor contacts = contentResolver.query(uri, null, null, null, null);

                    if (contacts == null)
                        Log.d(TAG, "未发现sim 卡数据");
                    else {
                        Log.d(TAG, "从sim 卡中读取：" + contacts.getCount() + "");

                        // the fileds of sim 卡
                        String field_id = "_id", field_name = "name", field_number = "number", field_emails = "emails";

                        while (contacts.moveToNext()) {
                            Contact contact = new Contact();
                            String strMobile = contacts.getString(contacts
                                    .getColumnIndex(field_number));
                            contact.mobile = getFormalPhoneString(strMobile);
                            contact.name = contacts.getString(contacts.getColumnIndex(field_name));
                            contact.contactId = contacts.getLong(contacts.getColumnIndex(field_id));
                            Log.d(TAG, "contact from Sim :" + contact);
                            list.add(contact);

                        }
                        // contacts.moveToFirst();
                        // String[] columns = contacts.getColumnNames();
                        // for (int i = 0; i < columns.length; i++) {
                        // String key = contacts.getColumnName(i);
                        // String value =
                        // contacts.getString(contacts.getColumnIndex(key));
                        // L.d("simcontact", key + " = " + value);
                        // }
                        contacts.close();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                // 过滤重复
                if (list != null) {
                    for (int i = 0; i < list.size() - 1; i++) {
                        for (int j = list.size() - 1; j > i; j--) {
                            if (list.get(j).mobile.equals(list.get(i).mobile)) {
                                list.remove(j);
                            }
                        }
                    }
                }
                // 排序
                ArrayUtils.SortList(list, new Comparator<Contact>() {

                    @Override
                    public int compare(Contact lhs, Contact rhs) {

                        return lhs.firstPinyin - rhs.firstPinyin;
                    }
                });
                // if (list.size() > 0) {
                // // 缓存查询结果。
                // recentDataUtils.appendRecentData(context, list);
                // }
            }

            return list;
        } else
        {
            throw new Exception("通讯录访问被拒绝");
        }
    }
}
