package com.kent.newsdemo.util;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * author Kent
 * date 2018/7/30 030
 * version 1.0
 */
public class LogUtil {

    private static final boolean ENABLE_LOG = true;
    private static final String LIVE_LOG_TAG = "ldwlog";
    private static final String GLOBAL_TAG = "NewsDemo.";

    private static boolean sIsLiveLogOn;
    static {
        //adb shell setprop log.tag.ldwlog D
        sIsLiveLogOn = Log.isLoggable(LIVE_LOG_TAG, Log.DEBUG);
    }

    public static void v(String msg) {
        if (ENABLE_LOG || sIsLiveLogOn) {
            String[] logInfo = getLogInfo();
            Log.v(logInfo[0], logInfo[1] + "-->" + msg);
        }
    }

    public static void d(String msg) {
        if (ENABLE_LOG || sIsLiveLogOn) {
            String[] logInfo = getLogInfo();
            Log.d(logInfo[0], logInfo[1] + "-->" + msg);
        }
    }

    public static void i(String msg) {
        if (ENABLE_LOG || sIsLiveLogOn) {
            String[] logInfo = getLogInfo();
            Log.i(logInfo[0], logInfo[1] + "-->" + msg);
        }
    }

    public static void w(String msg) {
        if (ENABLE_LOG || sIsLiveLogOn) {
            String[] logInfo = getLogInfo();
            Log.w(logInfo[0], logInfo[1] + "-->" + msg);
        }
    }

    public static void e(String msg) {
        if (ENABLE_LOG || sIsLiveLogOn) {
            String[] logInfo = getLogInfo();
            Log.e(logInfo[0], logInfo[1] + "-->" + msg);
        }
    }

    @NonNull
    private static String[] getLogInfo() {
        StackTraceElement e = Thread.currentThread().getStackTrace()[4];
        String tag = e.getFileName();
        String method = e.getMethodName();
        int endIndex = tag.indexOf(".java");
        tag = tag.substring(0, endIndex);
        return new String[]{GLOBAL_TAG + tag, method + "()"};
    }

}
