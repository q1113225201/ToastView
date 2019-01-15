package com.sjl.libtoastview.util;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Util
 *
 * @author 林zero
 * @date 2019/1/14
 */
public class Util {
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

    private static Object iNotificationManagerObject;

    /**
     * 显示
     * 反射代理强制认定为系统Toast
     *
     * @param context
     * @param toast
     */
    public static void show(Context context, Toast toast) {
        if (context == null || toast == null) {
            throw new RuntimeException("context 与 toast不能为null");
        }
        if (isNotificationEnabled(context)) {
            toast.show();
        } else {
            try {
                Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
                getServiceMethod.setAccessible(true);
                if (iNotificationManagerObject == null) {
                    iNotificationManagerObject = getServiceMethod.invoke(null);
                    Class iNotificationManagerClazz = Class.forName("android.app.INotificationManager");
                    Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerClazz}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("enqueueToast".equals(method.getName())//原生系统用了enqueueToast
                                    || "enqueueToastEx".equals(method.getName())//华为P20用了enqueueToastEx
                                    ) {
                                //强制变成系统Toast
                                args[0] = "android";
                            }
                            return method.invoke(iNotificationManagerObject, args);
                        }
                    });
                    Field field = Toast.class.getDeclaredField("sService");
                    field.setAccessible(true);
                    //替换Toast里的sService
                    field.set(null, iNotificationManagerProxy);
                }
                toast.show();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
