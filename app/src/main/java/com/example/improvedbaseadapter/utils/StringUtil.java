
package com.example.improvedbaseadapter.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {

    public static String md5(String s) {
        /*
         * try { // Create MD5 Hash MessageDigest digest =
         * java.security.MessageDigest.getInstance("MD5");
         * digest.update(s.getBytes()); byte messageDigest[] = digest.digest();
         * // Create Hex String StringBuffer hexString = new StringBuffer(); for
         * (int i=0; i<messageDigest.length; i++)
         * hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
         * return hexString.toString(); } catch (NoSuchAlgorithmException e) {
         * e.printStackTrace(); } return "";
         */
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(), 0, s.length());
            String hash = new BigInteger(1, digest.digest()).toString(16);
            if (hash.length() % 2 != 0) {
                return "0" + hash;
            }
            return hash;
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static String join(String[] strings, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            if (i != 0)
                sb.append(separator);
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    public static boolean isNotEmptyString(String str) {
        return !isEmptyString(str);
    }

    public static boolean isEmptyString(String str) {
        return str == null || str.length() == 0 || str.equals("null");
    }

}
