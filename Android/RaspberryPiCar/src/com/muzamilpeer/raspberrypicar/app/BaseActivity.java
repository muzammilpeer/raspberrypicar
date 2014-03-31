
package com.muzamilpeer.raspberrypicar.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.muzamilpeer.raspberrypicar.R;

public class BaseActivity extends SlidingFragmentActivity {

    public static SlidingMenu slidingMenu;
    private static ActionbarUtil actionBarUtil;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        
        if(savedInstanceState == null) {
        }
        setBehindContentView(R.layout.left_menu_frame);
        slidingMenu = getSlidingMenu();
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setSecondaryMenu(R.layout.right_menu_frame);
        actionBarUtil = new ActionbarUtil(this);
        actionBarUtil.setup(true,mGlobal_OnClickListener);
        
    }
    
    public static void setActionBarTitle(String title) {
    	actionBarUtil.setTitle(title);
    }
    
    //Global On click listener for all views
    final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btnLeftMenu:
                	Log.e("Left Menu", "Left Menu Pressed");
                	slidingMenu.showMenu();
                break;
                case R.id.btnRightMenu:
                	Log.e("Right Menu", "Right Menu Pressed");
                	slidingMenu.showSecondaryMenu();
                break;
            }
        }
    };

}
