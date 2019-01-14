package com.sjl.toastview;

import android.app.Application;

import com.sjl.libtoastview.util.AppInit;

/**
 * App
 *
 * @author 林zero
 * @date 2019/1/5
 */
public class App extends Application {
    private static App app;

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AppInit.getInstance().init(this);
    }
}
