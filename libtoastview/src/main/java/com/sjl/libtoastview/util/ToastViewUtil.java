package com.sjl.libtoastview.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.sjl.libtoastview.widget.ToastView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Toast工具类
 *
 * @author 林zero
 * @date 2019/1/5
 */
public class ToastViewUtil {
    private static Map<Activity, List<ToastViewValue>> toastViewMap = new HashMap<>();

    /**
     * 获取
     *
     * @param activity
     * @param type     1 文字土司 2 视图土司
     * @param gravity
     * @param offsetX
     * @param offsetY
     * @return
     */
    private static ToastView getToastView(Activity activity, int type, int gravity, int offsetX, int offsetY) {
        ToastViewValue toastViewValue = new ToastViewValue(type, gravity, offsetX, offsetY);
        List<ToastViewValue> list = toastViewMap.get(activity);
        if (list == null) {
            list = new ArrayList<>();
        }
        int index = list.indexOf(toastViewValue);
        ToastView toastView;
        if (index == -1) {
            toastView = new ToastView(activity);
            toastView.setGravity(gravity, offsetX, offsetY);
            toastViewValue.setToastView(toastView);
            list.add(toastViewValue);
            toastViewMap.put(activity, list);
        } else {
            toastView = list.get(index).getToastView();
        }
        return toastView;
    }

    public static void removeToastView(Activity activity) {
        if(toastViewMap.containsKey(activity)) {
            for (ToastViewValue item : toastViewMap.get(activity)) {
                item.getToastView().cancel();
            }
        }
        toastViewMap.remove(activity);
    }

    public static void showToast(String msg) {
        showToast(msg, Gravity.BOTTOM, 0, 200);
    }

    public static void showToast(String msg, int gravity, int offsetX, int offsetY) {
        ToastView toastView = getToastView(getActivity(), 1, gravity, offsetX, offsetY);
        toastView.setText(msg);
        toastView.show();
    }

    public static void showToast(View view) {
        showToast(view, Gravity.BOTTOM, 0, 200);
    }

    public static void showToast(View view, int gravity, int offsetX, int offsetY) {
        ToastView toastView = getToastView(getActivity(), 2, gravity, offsetX, offsetY);
        toastView.setContentView(view);
        toastView.show();
    }

    private static Activity getActivity() {
        Activity activity = AppInit.getInstance().getTopActivity();
        if (activity == null) {
            throw new RuntimeException("请初始化AppInit");
        } else {
            return activity;
        }
    }

    static class ToastViewValue {
        int type;
        int gravity;
        int offsetX;
        int offsetY;
        ToastView toastView;

        public ToastViewValue(int type, int gravity, int offsetX, int offsetY) {
            this.type = type;
            this.gravity = gravity;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public ToastView getToastView() {
            return toastView;
        }

        public void setToastView(ToastView toastView) {
            this.toastView = toastView;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ToastViewValue that = (ToastViewValue) o;
            return type == that.type &&
                    gravity == that.gravity &&
                    offsetX == that.offsetX &&
                    offsetY == that.offsetY;
        }
    }
}
