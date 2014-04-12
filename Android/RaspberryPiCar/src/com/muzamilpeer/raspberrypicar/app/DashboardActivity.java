
package com.muzamilpeer.raspberrypicar.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.GetChars;
import android.view.View;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.accelerometerdrive.AccelerometerDriveFragment;
import com.muzamilpeer.raspberrypicar.app.camdrive.CamDriveFragment;
import com.muzamilpeer.raspberrypicar.app.help.HelpFragment;
import com.muzamilpeer.raspberrypicar.app.manualdrive.ManualDriveFragment;
import com.muzamilpeer.raspberrypicar.app.menu.LeftMenuFragment;
import com.muzamilpeer.raspberrypicar.app.menu.RightMenuFragment;
import com.muzamilpeer.raspberrypicar.app.scan.ScanCarFragment;
import com.muzamilpeer.raspberrypicar.app.settings.SettingsFragment;
import com.muzamilpeer.raspberrypicar.app.smartcamdrive.SmartCamDriveFragment;
import com.muzamilpeer.raspberrypicar.app.smartdrive.SmartDriveFragment;

public class DashboardActivity extends BaseActivity {
	
	private static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        if (savedInstanceState == null) {
        	fragmentManager = getSupportFragmentManager();
        	
        	fragmentManager.beginTransaction().replace(R.id.content_frame, new ScanCarFragment()).commit();
        	fragmentManager.beginTransaction().replace(R.id.left_menu_frame, new LeftMenuFragment()).commit();
        	fragmentManager.beginTransaction().replace(R.id.right_menu_frame, new RightMenuFragment()).commit();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }


    
    /*
    public static void refreshMainViewByNew(SherlockFragment fragment)
    {
    	if(fragment instanceof ScanCarFragment) {
    		fragmentManager.beginTransaction()
	         .replace(R.id.content_frame, new ScanCarFragment()).commitAllowingStateLoss();
    	}else if(fragment instanceof ManualDriveFragment) {
    		fragmentManager.beginTransaction()
	         .replace(R.id.content_frame, new ManualDriveFragment()).commitAllowingStateLoss();
    	}else if(fragment instanceof AccelerometerDriveFragment) {
    		fragmentManager.beginTransaction()
	         .replace(R.id.content_frame, new AccelerometerDriveFragment()).commit();
    	}else if(fragment instanceof SettingsFragment) {
    		fragmentManager.beginTransaction()
	         .replace(R.id.content_frame, new SettingsFragment()).commit();
    	}else if(fragment instanceof HelpFragment) {
			fragmentManager.beginTransaction()
	         .replace(R.id.content_frame, new HelpFragment()).commit();
    	}else if(fragment instanceof SmartDriveFragment) {
			fragmentManager.beginTransaction()
	         .replace(R.id.content_frame, new SmartDriveFragment()).commit();
    	}else if(fragment instanceof CamDriveFragment) {
			fragmentManager.beginTransaction()
	         .replace(R.id.content_frame, new CamDriveFragment()).commit();
    	}else if(fragment instanceof SmartCamDriveFragment) {
			fragmentManager.beginTransaction()
	         .replace(R.id.content_frame, new SmartCamDriveFragment()).commit();
		}
    	slidingMenu.showContent();
    	new onFriendAddedAsyncTask().execute(name);
    }
    */
    
    
    public static void refreshMainViewByNew(SherlockFragment fragment)
    {

        class ReplaceFragmentAsyncTask extends AsyncTask<String, Void, String> {
            private final FragmentTransaction ft;
            private final SherlockFragment fragment;
            
            ReplaceFragmentAsyncTask(FragmentTransaction ft,SherlockFragment fragment) {
                this.ft = ft;
                this.fragment = fragment;
            }
/*
            @Override
            protected void onPreExecute() {

                new Handler().post(new Runnable() {
                    public void run() {
	                	ft.add(layoutId, fragment);
                        ft.commit();
                    }
                });
            }
*/
            @Override
            protected void onPostExecute(String result) {
                new Handler().post(new Runnable() {
                    public void run() {
	                    ft.replace(R.id.content_frame, fragment);
	                    ft.commit();
	                   	slidingMenu.showContent();
                    }
                });
            }
    		@Override
    		protected String doInBackground(String... arg0) {
    			// TODO Auto-generated method stub
    			return null;
    		}
        }    	
    	
    	ReplaceFragmentAsyncTask task = new ReplaceFragmentAsyncTask(fragmentManager.beginTransaction(), fragment);
    	task.execute("Replace Fragment");
    }    
    

}
