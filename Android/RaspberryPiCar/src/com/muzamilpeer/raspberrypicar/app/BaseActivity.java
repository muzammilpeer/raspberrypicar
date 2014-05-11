
package com.muzamilpeer.raspberrypicar.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.service.RaspberryService;

public class BaseActivity extends SlidingFragmentActivity {

    public static SlidingMenu slidingMenu;
    private static ActionbarUtil actionBarUtil;
	private static final String TAG = "BroadcastTest";
	private Intent intent;
    
    
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
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        actionBarUtil = new ActionbarUtil(this);
        actionBarUtil.setup(true,mGlobal_OnClickListener);
        
        
        
        intent = new Intent(this, RaspberryService.class);        
    }
    
    public static void  setRefreshButtonListener(View.OnClickListener listener) {
    	actionBarUtil.setRefreshButtonListener(listener);
    }

    public ActionbarUtil getActionBarUtil() {
    	return actionBarUtil;
    }
    public static void showLoader() {
    	actionBarUtil.showLoader();
    }
    public static void hideLoader() {
    	actionBarUtil.hideLoader();
    }
    
    public static void setActionBarTitle(String title) {
    	actionBarUtil.setTitle(title);
    }
    
    public static void setCurrentItem(String count) {
    	actionBarUtil.setCurrentItem(count);
    }
    
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateUI(intent);       
        }
    };    
    
	@Override
	public void onResume() {
		super.onResume();		
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(RaspberryService.BROADCAST_ACTION));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		stopService(intent); 		
	}	
	    
    private void updateUI(Intent intent) {
    	String counter = intent.getStringExtra("counter"); 
    	String time = intent.getStringExtra("time");
    	Log.e(TAG, counter);
    	Log.e(TAG, time);
    	
//    	TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);  	
//    	TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
//    	txtDateTime.setText(time);
//    	txtCounter.setText(counter);
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
