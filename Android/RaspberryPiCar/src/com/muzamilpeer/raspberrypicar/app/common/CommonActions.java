
package com.muzamilpeer.raspberrypicar.app.common;


import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.RaspberryPiCarApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.TextView;

public class CommonActions {

    Activity currentActivity;

    MyProgressDialog myProgressDialog;

    Typeface trebuchet_MS_Bold;

    Typeface trebuchet_MS;
    public static int requestCount = 0;

    public CommonActions(Activity activity) {
        // TODO Auto-generated constructor stub
        this.currentActivity = activity;

        myProgressDialog = new MyProgressDialog(this.currentActivity);
        /*
         * trebuchet_MS_Bold = Typeface.createFromAsset(
         * currentActivity.getAssets(), "Trebuchet_MS_Bold.ttf"); trebuchet_MS =
         * Typeface.createFromAsset(currentActivity.getAssets(),
         * "Trebuchet_MS.ttf");
         */

    }

    public CommonActions(Context activity) {
        // TODO Auto-generated constructor stub
        //this.currentActivity = (Activity)activity;
        myProgressDialog = new MyProgressDialog(activity);

        // trebuchet_MS_Bold = Typeface.createFromAsset(
        // currentActivity.getAssets(), "Trebuchet_MS_Bold.ttf");
        // trebuchet_MS = Typeface.createFromAsset(currentActivity.getAssets(),
        // "Trebuchet_MS.ttf");
    }

    public void showProgress() {
        try {
        	if(requestCount ==0) {
			    myProgressDialog = myProgressDialog.show(this.currentActivity, null, "Please wait...",
			            true);
        	}
            requestCount++;
        } catch (BadTokenException e) {

        } catch (Exception e) {

        }
    }

    public void showProgress(String msg) {
        try {
            myProgressDialog = myProgressDialog.show(this.currentActivity, null, msg, true);
        } catch (BadTokenException e) {

        } catch (Exception e) {
        	MyLog.e("Progress Exception", e.getMessage());
        }
    }

    public void hideProgress() {
    	requestCount--;
    	if(requestCount < 1) {
	        if (myProgressDialog != null) {
	            myProgressDialog.cancel(); 
	        }
	        myProgressDialog.dismiss();
    	}
    }

    public void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences(
                RaspberryPiCarApplication.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void clearAllPreferences() {
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences(
        		RaspberryPiCarApplication.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void saveUserPreferences(String key, String value) {
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences(
        		RaspberryPiCarApplication.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValueString(String key, String default_value) {

        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences(
        		RaspberryPiCarApplication.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, default_value);
    }

    public boolean hasConnection() {

        try {
            ConnectivityManager cm = (ConnectivityManager)currentActivity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnected()) {
                return true;
            }

            NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null && mobileNetwork.isConnected()) {
                return true;
            }

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return false;
    }

    public Typeface getTrebuchet_MS_Bold() {

        return trebuchet_MS_Bold;

    }

    public Typeface getTrebuchet_MS() {

        return trebuchet_MS;

    }

    public void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences(
        		RaspberryPiCarApplication.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getTabString(String key, int default_value) {

        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences(
        		RaspberryPiCarApplication.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, default_value);
    }

    public static void alertDialogShow(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setView(null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlert(Context context, String title, String message) {
        final Dialog dialog = new Dialog(context,R.style.OneLoad_Dialog);
        dialog.setContentView(R.layout.dialog_custom_view);
        dialog.setTitle(title);
        

        ((TextView)dialog.findViewById(R.id.tvDialogMessage)).setText(message);

        ((Button)dialog.findViewById(R.id.bDialogOk))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        
                        dialog.dismiss();
                    }
                });
        
        
        dialog.show();
    }
}
