package com.sjl.libtoastview.widget;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sjl.libtoastview.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ToastView
 *
 * @author 林zero
 * @date 2019/1/5
 */
public class ToastView {
    private static final int LENGTH_LONG = 3500;
    private static final int LENGTH_SHORT = 2000;
    /**
     * 文字toast
     */
    private Toast textToast;
    /**
     * 视图toast
     */
    private Toast viewToast;
    /**
     * 无权限时用PopupWindow
     */
    private PopupWindow popupWindow;
    /**
     * PopupWindow内容容器
     */
    private LinearLayout contentParentView;
    /**
     * 内容视图
     */
    private View contentView;
    /**
     * 默认视图
     */
    private View defaultView;
    /**
     * toast属性
     */
    private int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int offsetX;
    private int offsetY = 200;
    private int duration;
    private CharSequence text;

    private Activity activity;

    public ToastView(Activity activity) {
        this(activity, null, Toast.LENGTH_SHORT);
    }

    public ToastView(Activity activity, CharSequence text, int duration) {
        this.activity = activity;
        this.text = text;
        this.duration = duration;
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_toast_parent, null);
        contentParentView = view.findViewById(R.id.toast_parent);
    }

    private void initToast() {
        if (isNotificationEnabled(activity)) {
            if (textToast == null) {
                textToast = Toast.makeText(activity, "", duration);
                textToast.setDuration(duration);
                textToast.setGravity(gravity, offsetX, offsetY);
            }
            if (viewToast == null) {
                viewToast = Toast.makeText(activity, "", duration);
                viewToast.setDuration(duration);
                viewToast.setGravity(gravity, offsetX, offsetY);
                viewToast.setView(contentParentView);
            }
        } else if (popupWindow == null) {
            popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(false);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(false);
            popupWindow.setContentView(contentParentView);
        }
    }

    public static ToastView makeText(Activity activity, CharSequence text, int duration) {
        ToastView toastView = new ToastView(activity, text, duration);
        return toastView;
    }

    public void show() {
        initToast();
        startTimer();
        if (isNotificationEnabled(activity)) {
            if (contentView != null) {
                setCustomView();
                viewToast.show();
            } else {
                textToast.setGravity(gravity, offsetX, offsetY);
                textToast.setText(text);
                textToast.show();
            }
        } else {
            if (contentView != null) {
                setCustomView();
                popupWindow.setContentView(contentParentView);
            } else {
                ((TextView) defaultView.findViewById(R.id.tv_default_text)).setText(text);
                popupWindow.setContentView(defaultView);
            }
            if (!popupWindow.isShowing()) {
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), gravity, offsetX, offsetY);
            }
        }
    }

    private Timer timer;
    private TimerTask timerTask;

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ToastView.this.cancel();
            }
        };
        timer.schedule(timerTask, duration == Toast.LENGTH_SHORT ? LENGTH_SHORT : LENGTH_LONG);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if(timerTask!=null){
            timerTask.cancel();
            timerTask = null;
        }
    }

    /**
     * 设置View视图
     */
    private void setCustomView() {
        contentParentView.removeAllViews();
        if (contentView.getParent() != null) {
            ((ViewGroup) contentView.getParent()).removeView(contentView);
        }
        contentParentView.addView(contentView);
    }

    public void cancel() {
        stopTimer();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        if (viewToast != null) {
            viewToast.cancel();
        }
        if (textToast != null) {
            textToast.cancel();
        }
    }

    /**
     * 检查通知栏权限有没有开启
     */
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return (Integer) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) == 0;
            } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException | RuntimeException | ClassNotFoundException ignored) {
                return true;
            }
        } else {
            return true;
        }
    }

    public void setText(CharSequence text) {
        this.text = text;
        this.contentView = null;
        if (this.defaultView == null) {
            this.defaultView = LayoutInflater.from(activity).inflate(R.layout.layout_toast_default, null);
        }
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
        this.defaultView = null;
        this.text = null;
    }

    public void setGravity(int gravity, int offsetX, int offsetY) {
        this.gravity = gravity;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
