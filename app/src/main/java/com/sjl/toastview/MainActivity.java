package com.sjl.toastview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sjl.libtoastview.util.ToastViewUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_toast_msg).setOnClickListener(this);
        findViewById(R.id.btn_toast_msg_gravity).setOnClickListener(this);
        findViewById(R.id.btn_toast_view).setOnClickListener(this);
        findViewById(R.id.btn_toast_view_gravity).setOnClickListener(this);
        findViewById(R.id.btn_toast_system).setOnClickListener(this);
        findViewById(R.id.btn_toast_system_gravity).setOnClickListener(this);
        findViewById(R.id.btn_toast_system_view).setOnClickListener(this);
        findViewById(R.id.btn_toast_system_view_gravity).setOnClickListener(this);
    }

    private int cnt = 0;

    @Override
    public void onClick(View v) {
        cnt++;
        switch (v.getId()) {
            case R.id.btn_toast_msg:
                ToastViewUtil.showToast("cnt=" + cnt);
                break;
            case R.id.btn_toast_msg_gravity:
                ToastViewUtil.showToast("cnt=" + cnt, Gravity.CENTER, 0, 0);
                break;
            case R.id.btn_toast_view:
                ToastViewUtil.showToast(buildView());
                break;
            case R.id.btn_toast_view_gravity:
                ToastViewUtil.showToast(buildView(), Gravity.CENTER, 200, 0);
                break;
            case R.id.btn_toast_system:
                ToastUtil.showToast("cnt=" + cnt);
                break;
            case R.id.btn_toast_system_gravity:
                ToastUtil.showToast("cnt=" + cnt, Gravity.CENTER, 0, 0);
                break;
            case R.id.btn_toast_system_view:
                ToastUtil.showToast(buildView());
                break;
            case R.id.btn_toast_system_view_gravity:
                ToastUtil.showToast(buildView(), Gravity.CENTER, 0, 0);
                break;
        }
    }

    private View buildView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_toast_view, null);
        ((TextView) view.findViewById(R.id.tv_toast)).setText("cnt=" + cnt);
        return view;
    }
}
