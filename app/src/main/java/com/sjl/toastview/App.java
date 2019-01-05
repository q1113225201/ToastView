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
    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.getInstance().init(this);
    }
}
