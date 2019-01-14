package com.sjl.toastview;

import android.widget.Toast;

import com.sjl.libtoastview.util.Util;

/**
 * ToastUtil
 *
 * @author 林zero
 * @date 2019/1/14
 */
public class ToastUtil {
    private static Toast toast;

    public static void showToast(String msg) {
        Toast toast = Toast.makeText(App.getApp(), msg, Toast.LENGTH_SHORT);
        Util.show(App.getApp(), toast);
    }
}
