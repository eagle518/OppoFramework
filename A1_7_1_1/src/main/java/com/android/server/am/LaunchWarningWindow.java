package com.android.server.am;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

public final class LaunchWarningWindow extends Dialog {
    public LaunchWarningWindow(Context context, ActivityRecord cur, ActivityRecord next) {
        super(context, 16974977);
        requestWindowFeature(3);
        getWindow().setType(2003);
        getWindow().addFlags(24);
        setContentView(17367159);
        setTitle(context.getText(17040300));
        TypedValue out = new TypedValue();
        getContext().getTheme().resolveAttribute(16843605, out, true);
        getWindow().setFeatureDrawableResource(3, out.resourceId);
        ((ImageView) findViewById(16909199)).setImageDrawable(next.info.applicationInfo.loadIcon(context.getPackageManager()));
        ((TextView) findViewById(16909200)).setText(context.getResources().getString(17040301, new Object[]{next.info.applicationInfo.loadLabel(context.getPackageManager()).toString()}));
        ((ImageView) findViewById(16909201)).setImageDrawable(cur.info.applicationInfo.loadIcon(context.getPackageManager()));
        ((TextView) findViewById(16909202)).setText(context.getResources().getString(17040302, new Object[]{cur.info.applicationInfo.loadLabel(context.getPackageManager()).toString()}));
    }
}
