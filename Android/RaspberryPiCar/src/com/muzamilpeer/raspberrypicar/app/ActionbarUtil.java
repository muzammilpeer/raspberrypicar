package com.muzamilpeer.raspberrypicar.app;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.muzamilpeer.raspberrypicar.R;

public class ActionbarUtil {

    private View actionbarView;

    private ActionBar actionBar;
    
    private Activity activity;

    public ActionbarUtil(Context context) {
        actionbarView = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_actionbar,
                null);

        if (context instanceof BaseActivity) {
            actionBar = ((BaseActivity)context).getSupportActionBar();
            activity = ((BaseActivity)context);
        }

    }

    public void setup(boolean isParent, View.OnClickListener listener) {

        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View homeIcon = activity
                .findViewById(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.id.home
                        : R.id.abs__home);
        ((View)homeIcon.getParent()).setVisibility(View.GONE);

        actionBar.setCustomView(actionbarView);
        
        if (isParent) {
            ((ImageButton)actionbarView.findViewById(R.id.btnLeftMenu)).setOnClickListener(listener);
            ((ImageButton)actionbarView.findViewById(R.id.btnRightMenu)).setOnClickListener(listener);
            ((ImageButton)actionbarView.findViewById(R.id.btnConnectMenu)).setOnClickListener(listener);
            ((ImageButton)actionbarView.findViewById(R.id.btnRefreshMenu)).setOnClickListener(listener);
        }

    }

    public void setTitle(String title) {
        ((TextView)actionbarView.findViewById(R.id.actionbar_title)).setText(title);
    }

}
