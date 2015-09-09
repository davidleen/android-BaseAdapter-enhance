
package com.example.improvedbaseadapter.utils;

import java.io.InputStream;
import java.io.Serializable;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * 通讯录数据
 * 
 * @author davidleen29
 */
public class Contact implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public char firstPinyin;

    public String mobile;

    public String name;

    // public String bitmap;
    public long contactId;

    public boolean hasPhoto;

    public Bitmap loadBitmap(Context context)
    {
        // L.d("contact", "contact+id:" + contactid);
        if (contactId <= 0)
            return null;

        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                context.getContentResolver(), uri);
        Bitmap bitmap = null;
        if (input != null) {

            bitmap = BitmapFactory.decodeStream(input);
        }
        try {
            input.close();
        } catch (Throwable t) {
        }
        return bitmap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Contact [firstPinyin=");
        builder.append(firstPinyin);
        builder.append(", mobile=");
        builder.append(mobile);
        builder.append(", name=");
        builder.append(name);
        builder.append(", contactId=");
        builder.append(contactId);
        builder.append(", hasPhoto=");
        builder.append(hasPhoto);
        builder.append("]");
        return builder.toString();
    }

}
