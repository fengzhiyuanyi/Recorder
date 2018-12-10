package com.netease.testease;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {
    private static final int OVERLAY_PERMISSION = 756232212;
    AlertDialog mPermissionDialog;
    List<String> mPermissionList = new ArrayList();
    private final int mRequestCode = 100;
    String[] permissions = {"android.permission.RECORD_AUDIO", "android.permission.READ_EXTERNAL_STORAGE"};
    private View textView;

    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

    @TargetApi(23)
    private void initPermission() {
        this.mPermissionList.clear();
        int i = 0;
        while (i < this.permissions.length) {
            if (ActivityCompat.checkSelfPermission(this, this.permissions[i]) != 0) {
                this.mPermissionList.add(this.permissions[i]);
            }
            i += 1;
        }
        if (this.mPermissionList.size() > 0) {
            requestPermissions(this.permissions, mRequestCode);
            return;
        }
        if (Settings.canDrawOverlays(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        requestAlertWindowPermission();
    }

    @TargetApi(23)
    private void requestAlertWindowPermission() {
        Intent localIntent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
        if (Build.VERSION.SDK_INT < 26) {
            localIntent.setData(Uri.parse("package:" + getPackageName()));
        }
        startActivityForResult(localIntent, OVERLAY_PERMISSION);
    }

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予").setPositiveButton("设置", (dialog, which) -> {
                        cancelPermissionDialog();
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        startActivity(intent);
                    }).setNegativeButton("取消", (dialog, which) -> {
                        //关闭页面或者做其他操作
                        cancelPermissionDialog();
                    }).create();
        }
        mPermissionDialog.show();
    }

    @TargetApi(23)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, R.string.permission_failure_tip, Toast.LENGTH_SHORT).show();
                this.textView.postDelayed(() -> finish(), 1000L);
            }else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.textView = findViewById(R.id.img_launch);
        initPermission();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean exist = false;
        if (mRequestCode == requestCode) {
            for(int result : grantResults){
                if(result == -1){
                    exist = true;
                }
            }
            if (exist) {
                Toast.makeText(this, R.string.permission_failure_tip, Toast.LENGTH_SHORT).show();
                this.textView.postDelayed(() -> finish(), 1000L);
                return;
            }
        }
        if ((Build.VERSION.SDK_INT >= 23) && (!Settings.canDrawOverlays(this))) {
            requestAlertWindowPermission();
            return;
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
