
package com.example.improvedbaseadapter;

import android.text.TextUtils;

/**
 * 日志封装类
 */
public class Log {
    private final static boolean logFlag = true;

    private final static int logLevel = android.util.Log.VERBOSE;

    public static String tag = "";

    private static Log instance = null;

    public static final int VERBOSE = android.util.Log.VERBOSE;

    // Field descriptor
    public static final int DEBUG = android.util.Log.DEBUG;

    // Field descriptor
    public static final int INFO = android.util.Log.INFO;

    // Field descriptor
    public static final int WARN = android.util.Log.WARN;

    // Field descriptor
    public static final int ERROR = android.util.Log.ERROR;

    // Field descriptor
    public static final int ASSERT = android.util.Log.ASSERT;

    private static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    /**
     * 获取运行时信息
     * 
     * @return
     */
    private String getFunctionName()
    {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null)
        {
            return null;
        }
        for (StackTraceElement st : sts)
        {
            // Log.d("a", "st:" + st.toString());
            if (st.isNativeMethod())
            {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName()))
            {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName()))
            {
                continue;
            }
            tag = st.getClassName();
            return "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " "
                    + st.getMethodName() + " ]";
        }
        return null;
    }

    /**
     * Send an INFO log message
     * 
     * @param msg
     */
    public static void i(String msg)
    {
        i(null, msg);
    }

    /**
     * Send an INFO log message
     * 
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    /**
     * Send an INFO log message
     * 
     * @param tag
     * @param msg
     * @param tr
     */
    public static void i(String tag, String msg, Throwable tr)
    {
        if (logFlag)
        {
            if (logLevel <= android.util.Log.INFO)
            {
                String name = getInstance().getFunctionName();
                if (name != null)
                {
                    msg = new StringBuilder(name).append(" - ").append(msg).toString();
                }

                if (TextUtils.isEmpty(tag)) {
                    tag = Log.tag;
                }

                if (tr == null) {
                    android.util.Log.i(tag, msg);
                } else {
                    android.util.Log.i(tag, msg, tr);
                }
            }
        }
    }

    /**
     * Send an DEBUG log message
     * 
     * @param msg
     */
    public static void d(String msg)
    {
        d(null, msg);
    }

    /**
     * Send a DEBUG log message
     * 
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg)
    {
        d(tag, msg, null);
    }

    /**
     * Send an DEBUG log message
     * 
     * @param tag
     * @param msg
     * @param tr
     */
    public static void d(String tag, String msg, Throwable tr)
    {
        if (logFlag)
        {
            if (logLevel <= android.util.Log.DEBUG)
            {
                String name = getInstance().getFunctionName();
                if (name != null)
                {
                    msg = new StringBuilder(name).append(" - ").append(msg).toString();
                }

                if (TextUtils.isEmpty(tag)) {
                    tag = Log.tag;
                }

                if (tr == null) {
                    android.util.Log.d(tag, msg);
                } else {
                    android.util.Log.d(tag, msg, tr);
                }
            }
        }
    }

    /**
     * Send an VERBOSE log message
     * 
     * @param msg
     */
    public static void v(String msg)
    {
        v(null, msg);
    }

    /**
     * Send a VERBOSE log message
     * 
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg)
    {
        v(tag, msg, null);
    }

    /**
     * Send an VERBOSE log message
     * 
     * @param tag
     * @param msg
     * @param tr
     */
    public static void v(String tag, String msg, Throwable tr)
    {
        if (logFlag)
        {
            if (logLevel <= android.util.Log.VERBOSE)
            {
                String name = getInstance().getFunctionName();
                if (name != null)
                {
                    msg = new StringBuilder(name).append(" - ").append(msg).toString();
                }

                if (TextUtils.isEmpty(tag)) {
                    tag = Log.tag;
                }

                if (tr == null) {
                    android.util.Log.v(tag, msg);
                } else {
                    android.util.Log.v(tag, msg, tr);
                }
            }
        }
    }

    /**
     * Send an WARN log message
     * 
     * @param msg
     */
    public static void w(String msg)
    {
        w(null, msg);
    }

    /**
     * Send a WARN log message
     * 
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg)
    {
        w(tag, msg, null);
    }

    /**
     * Send an WARN log message
     * 
     * @param tag
     * @param msg
     * @param tr
     */
    public static void w(String tag, String msg, Throwable tr)
    {
        if (logFlag)
        {
            if (logLevel <= android.util.Log.WARN)
            {
                String name = getInstance().getFunctionName();
                if (name != null)
                {
                    msg = new StringBuilder(name).append(" - ").append(msg).toString();
                }

                if (TextUtils.isEmpty(tag)) {
                    tag = Log.tag;
                }

                if (tr == null) {
                    android.util.Log.w(tag, msg);
                } else {
                    android.util.Log.w(tag, msg, tr);
                }
            }
        }
    }

    /**
     * Send an ERROR log message
     * 
     * @param msg
     */
    public static void e(String msg)
    {
        e(null, msg);
    }

    /**
     * Send an ERROR log message
     * 
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg)
    {
        e(tag, msg, null);
    }

    /**
     * Send an ERROR log message
     * 
     * @param tag
     * @param msg
     * @param tr
     */
    public static void e(String tag, String msg, Throwable tr)
    {
        if (logFlag)
        {
            if (logLevel <= android.util.Log.ERROR)
            {
                String name = getInstance().getFunctionName();
                if (name != null)
                {
                    msg = new StringBuilder(name).append(" - ").append(msg).toString();
                }

                if (TextUtils.isEmpty(tag)) {
                    tag = Log.tag;
                }

                if (tr == null) {
                    android.util.Log.e(tag, msg);
                } else {
                    android.util.Log.e(tag, msg, tr);
                }
            }
        }
    }

    /**
     * Checks to see whether or not a log for the specified tag is loggable at
     * the specified level
     * 
     * @param tag
     * @param level
     * @return
     */
    public static boolean isLoggable(String tag, int level) {
        return android.util.Log.isLoggable(tag, level);
    }

    /**
     * Low-level logging call
     * 
     * @param priority
     * @param tag
     * @param msg
     * @return
     */
    public static int println(int priority, String tag, String msg) {
        return android.util.Log.println(priority, tag, msg);
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     * 
     * @param tr
     * @return
     */
    public static String getStackTraceString(Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }
    // public static int wtf(String tag, Throwable tr) {
    // return wtf(tag, null, tr);
    // }
    //
    // public static int wtf(String tag, String msg) {
    // return wtf(tag, msg, null);
    // }
    //
    // public static int wtf(String tag, String msg, Throwable tr) {
    // return android.util.Log.wtf(tag, msg, tr);
    // }
}
