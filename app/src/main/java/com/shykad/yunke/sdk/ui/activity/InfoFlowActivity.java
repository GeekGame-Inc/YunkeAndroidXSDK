package com.shykad.yunke.sdk.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.shykad.yunke.sdk.R;
import com.shykad.yunke.sdk.engine.permission.config.PermissionConfig;
import com.shykad.yunke.sdk.utils.LogUtils;

import java.util.List;

import androidx.annotation.Nullable;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.REQUEST_INSTALL_PACKAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Create by wanghong.he on 2019/3/20.
 * description：信息流广告
 */
public class InfoFlowActivity extends PermissionActivity {

    private Button listBtn,recycleBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yunke_activity_infoflow_ad);
        init();

    }

    private void init() {
        listBtn = findViewById(R.id.infoflow_listview_btn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionInstallTask();
//                adJump(InfoFlowListViewActivity.class);
            }
        });
        recycleBtn = findViewById(R.id.infoflow_recycleview_btn);
        recycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adJump(InfoFlowRecycleViewActivity.class);
            }
        });
    }

    private void adJump(Class<?> cls){

        startActivity(new Intent(InfoFlowActivity.this, cls));
    }

    /**
     * 请求权限 强烈建议合适的时机调用 防止获取不了相应权限，下载广告应用没有安装或应用安装失败的问题
     * 权限请求如不满足需求，请自行开发
     */
    public void permissionInstallTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            boolean installs = getPackageManager().canRequestPackageInstalls();
            if (installs){
                //需要用户手动运行，拥有权限去执行
                LogUtils.i("已获取install package权限");
            }else {
                String[] perms = {REQUEST_INSTALL_PACKAGES};
                performCodeWithPermission(getString(R.string.rationale_install_package), PermissionConfig.REQUEST_INSTALL_PACKAGES_PERM, perms, new PermissionCallback() {
                    @Override
                    public void hasPermission(List<String> allPerms) {
                        //需要用户手动运行，拥有权限去执行
                        LogUtils.i("已获取install package权限");
                    }

                    @Override
                    public void noPermission(List<String> deniedPerms, List<String> grantedPerms, Boolean hasPermanentlyDenied) {
                        if (hasPermanentlyDenied) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                            startActivityForResult(intent, PermissionConfig.REQUEST_INSTALL_PACKAGES_PERM);
                            LogUtils.i("获取install package权限被拒");
                        }
                    }
                });
            }
        }

    }
}
