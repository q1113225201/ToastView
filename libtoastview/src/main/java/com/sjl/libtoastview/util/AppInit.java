package com.sjl.libtoastview.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * AppInit
 *
 * @author 林zero
 * @date 2019/1/5
 */
public class AppInit {
    private static class ClassHolder {
        private static AppInit appInit = new AppInit();
    }

    private AppInit() {
    }

    public static AppInit getInstance() {
        return ClassHolder.appInit;
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppInit.this.activity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ToastUtil.removeToastView(activity);
            }
        });
    }

    private Activity activity;

    public Activity getTopActivity() {
        return activity;
    }
}
