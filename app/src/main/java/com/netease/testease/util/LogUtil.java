package com.netease.testease.util;

import android.util.Log;


/**
 * Created by yhao on 2017/12/29.
 * https://github.com/yhaolpz
 */

class LogUtil {

    private static final String TAG = "ScreenRecord";


    static void e(String message) {

        Log.e(TAG, message);
    }


    static void d(String message) {

        Log.d(TAG, message);
    }


}
