package com.muzamilpeer.raspberrypicar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.common.MyLog;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.service.RaspberryService;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RaspberryPiApplication extends Application {

	public static String SERVER_IP, SERVER_PORT;
	public static final String APP_SETTINGS_FILE = "fa_preferences";

	public static final String APP_TAG = "fa_log";

	public static boolean isOnChild = false;

	public static String appLanguage = "en";
	private static final String TAG = "BroadcastTest";
	private Intent intent;

	private Thread recieveThread;
	private boolean isConnected = false;
	Socket s;
	BufferedReader in;
	BufferedWriter out;

	private static RaspberryPiApplication instance = null;

	/*
	 * private OneLoadApplication() { // Exists only to defeat instantiation. }
	 */


	public RaspberryPiApplication getInstance() {
		if (instance == null) {
			instance = new RaspberryPiApplication();
			intent = new Intent(this, RaspberryService.class);
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void changeLocale(String lang) {
		Locale locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
	}

	// @Override
	// public void onResume() {
	// super.onResume();
	// startService(intent);
	// registerReceiver(broadcastReceiver, new
	// IntentFilter(RaspberryService.BROADCAST_ACTION));
	// }
	//
	// @Override
	// public void onPause() {
	// super.onPause();
	// unregisterReceiver(broadcastReceiver);
	// stopService(intent);
	// }

	private void updateUI(Intent intent) {
		String counter = intent.getStringExtra("counter");
		String time = intent.getStringExtra("time");
		Log.e(TAG, counter);
		Log.e(TAG, time);

		// TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
		// TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
		// txtDateTime.setText(time);
		// txtCounter.setText(counter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateUI(intent);
		}
	};

}
