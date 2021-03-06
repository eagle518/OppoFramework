package com.android.server.am;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class UnsupportedDisplaySizeDialog {
    private final AlertDialog mDialog;
    private final String mPackageName;

    final /* synthetic */ class -void__init__com_android_server_am_ActivityManagerService_service_android_content_Context_context_android_content_pm_ApplicationInfo_appInfo_LambdaImpl0 implements OnCheckedChangeListener {
        private /* synthetic */ ActivityManagerService val$service;

        public /* synthetic */ -void__init__com_android_server_am_ActivityManagerService_service_android_content_Context_context_android_content_pm_ApplicationInfo_appInfo_LambdaImpl0(ActivityManagerService activityManagerService) {
            this.val$service = activityManagerService;
        }

        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            UnsupportedDisplaySizeDialog.this.m15-com_android_server_am_UnsupportedDisplaySizeDialog_lambda$1(this.val$service, arg0, arg1);
        }
    }

    public UnsupportedDisplaySizeDialog(ActivityManagerService service, Context context, ApplicationInfo appInfo) {
        this.mPackageName = appInfo.packageName;
        this.mDialog = new Builder(context).setPositiveButton(17039370, null).setMessage(context.getString(17040306, new Object[]{appInfo.loadSafeLabel(context.getPackageManager())})).setView(17367299).create();
        this.mDialog.create();
        Window window = this.mDialog.getWindow();
        window.setType(2002);
        window.getAttributes().setTitle("UnsupportedDisplaySizeDialog");
        CheckBox alwaysShow = (CheckBox) this.mDialog.findViewById(16909112);
        alwaysShow.setChecked(true);
        alwaysShow.setOnCheckedChangeListener(new -void__init__com_android_server_am_ActivityManagerService_service_android_content_Context_context_android_content_pm_ApplicationInfo_appInfo_LambdaImpl0(service));
    }

    /* renamed from: -com_android_server_am_UnsupportedDisplaySizeDialog_lambda$1 */
    /* synthetic */ void m15-com_android_server_am_UnsupportedDisplaySizeDialog_lambda$1(ActivityManagerService service, CompoundButton buttonView, boolean isChecked) {
        synchronized (service) {
            try {
                ActivityManagerService.boostPriorityForLockedSection();
                service.mCompatModePackages.setPackageNotifyUnsupportedZoomLocked(this.mPackageName, isChecked);
            } finally {
                ActivityManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void show() {
        this.mDialog.show();
    }

    public void dismiss() {
        this.mDialog.dismiss();
    }
}
