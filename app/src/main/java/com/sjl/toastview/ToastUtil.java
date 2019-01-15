package com.sjl.toastview;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * ToastUtil
 *
 * @author 林zero
 * @date 2019/1/14
 */
public class ToastUtil {
    private static Toast toast;
    private static Toast viewToast;

    private static void initToast() {
        if (toast == null) {
            toast = Toast.makeText(App.getApp(), "", Toast.LENGTH_SHORT);
        }
    }

    private static void initViewToast() {
        if (viewToast == null) {
            viewToast = Toast.makeText(App.getApp(), "", Toast.LENGTH_SHORT);
        }
    }

    public static void showToast(String text) {
        showToast(text, Gravity.BOTTOM, 0, 200);
    }

    public static void showToast(String text, int gravity, int offsetX, int offsetY) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        initToast();
        toast.setText(text);
        toast.setGravity(gravity, offsetX, offsetY);
        toast.show();
    }

    public static void showToast(View view) {
        showToast(view, Gravity.BOTTOM, 0, 200);
    }

    public static void showToast(View view, int gravity, int offsetX, int offsetY) {
        if (view == null) {
            return;
        }
        initViewToast();
        viewToast.setView(view);
        viewToast.setGravity(gravity, offsetX, offsetY);
        viewToast.show();
    }

}
