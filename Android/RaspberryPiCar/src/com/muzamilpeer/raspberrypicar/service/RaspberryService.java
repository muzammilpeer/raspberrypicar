package com.muzamilpeer.raspberrypicar.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.muzamilpeer.raspberrypicar.app.DashboardActivity;
import com.muzamilpeer.raspberrypicar.app.common.CommonUtils;
import com.muzamilpeer.raspberrypicar.app.common.L;
import com.muzamilpeer.raspberrypicar.app.common.MyLog;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.dblayer.DBAccess;
import com.muzamilpeer.raspberrypicar.dblayer.DatabaseHelper;
import com.muzamilpeer.raspberrypicar.dblayer.DatabaseManager;
import com.muzamilpeer.raspberrypicar.dblayer.QueryExecutor;
import com.muzamilpeer.raspberrypicar.dbmodel.DBServer;
import com.muzamilpeer.raspberrypicar.model.ServerInfoModel;
import com.muzamilpeer.raspberrypicar.networklayer.RaspberryPiNetworkService;
import com.muzamilpeer.raspberrypicar.networklayer.ResponseListener;
import com.muzamilpeer.raspberrypicar.networklayer.SocketResponseListener;

public class RaspberryService extends Service implements ResponseListener {
	private static final String TAG = "RaspberryService";
	public static final String BROADCAST_ACTION = "com.muzamilpeer.raspberrypicar.service.displayevent";
	private final Handler handler = new Handler();
	private String SERVER_IP, SERVER_PORT;

	Intent intent;
	int counter = 0;

	RaspberryPiNetworkService carService;

	Socket s;
	BufferedReader in;
	BufferedWriter out;
	Thread recieveThread, sendThread;

	SocketResponseListener responseListener;

	private int result = Activity.RESULT_CANCELED;
	public static final String URL = "urlpath";
	public static final String FILENAME = "filename";
	public static final String FILEPATH = "filepath";
	public static final String RESULT = "result";
	public static final String NOTIFICATION = "com.vogella.android.service.receiver";

	@Override
	public void onCreate() {
		super.onCreate();

		initObjects();
		initListeners();
	}

	private void initObjects() {

		intent = new Intent(BROADCAST_ACTION);
		this.carService = new RaspberryPiNetworkService(
				this.getApplicationContext());
	}

	public void setSocketResponseListener(SocketResponseListener listener) {
		this.responseListener = listener;
	}

	private void initListeners() {
		this.carService.setResponseListener(this);
	}

	private void publishResults(String outputPath, int result) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(FILEPATH, outputPath);
		intent.putExtra(RESULT, result);
		sendBroadcast(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		switch (intent.getExtras().getInt(SystemConstants.KEY_COMMAND)) {
		case SystemConstants.COMMAND_SCAN_NETWORK: {

		}
			break;
		case SystemConstants.COMMAND_CONNECT_SERVER: {
			this.SERVER_IP = intent.getExtras().getString(
					SystemConstants.KEY_SERVER_IP);
			this.SERVER_PORT = intent.getExtras().getString(
					SystemConstants.KEY_SERVER_PORT);
			this.initSocket();
			if (this.isConnectionAvailable()) {
				Runnable r = new RecieveThread("");
				new Thread(r).start();
			}
		}
			break;
		case SystemConstants.COMMAND_MOVE: {
			sendMessage(intent.getExtras().getString(SystemConstants.KEY_MOVE),
					intent.getExtras().getString(SystemConstants.KEY_TAG));
		}
			break;
		case SystemConstants.COMMAND_DISCONNECT_SERVER: {

		}
			break;

		default:
			break;
		}

		// handler.removeCallbacks(sendUpdatesToUI);
		// handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

	}

	private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {

			if (!carService.isRunning()) {
				carService.callScanningService();
			} else {
			}
			handler.postDelayed(this, 60000); // 60 seconds
		}
	};

	private void callService() {
	}

	private void DisplayLoggingInfo() {
		Log.e(TAG, "entered DisplayLoggingInfo");

		intent.putExtra("time", new Date().toLocaleString());
		intent.putExtra("counter", String.valueOf(++counter));
		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		handler.removeCallbacks(sendUpdatesToUI);
		super.onDestroy();
	}

	private void insertorUpdate(final DBServer model) throws Exception {

		L.LOG_TAG = "database";
		DatabaseManager.initializeInstance(new DatabaseHelper(
				getApplicationContext()));

		L.e("insertorUpdate record");

		DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
			@Override
			public void run(SQLiteDatabase database) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("server_ip", model.getServer_ip());
				new DBAccess(database, getApplicationContext()).insertorUpdate(
						map, model);
			}
		});
	}

	private void handeShackingMessage() {
		String inMsg = "";
		try {
			if (isConnectionAvailable()) {
				String iam = "iam:androidphone";
				out.write(iam);
				out.flush();
			}
		} catch (IOException e) {
			intent.putExtra("exception", e.getMessage());
			intent.putExtra("message", "");
			sendBroadcast(intent);
		}
	}

	public void initSocket() {
		try {
			if (s == null) {
				s = new Socket(this.SERVER_IP,
						Integer.parseInt(this.SERVER_PORT));
				in = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(
						s.getOutputStream()));
			}
		} catch (UnknownHostException e) {
			intent.putExtra("exception", e.getMessage());
			intent.putExtra("message", "");
			sendBroadcast(intent);
		} catch (IOException e) {
			intent.putExtra("exception", e.getMessage());
			intent.putExtra("message", "");
			sendBroadcast(intent);
		}
	}

	public class RecieveThread implements Runnable {

		public RecieveThread(Object parameter) {
			// store parameter for later user
		}

		@Override
		public void run() {
			try {
				while (in.readLine() != null) {
					String message = in.readLine()
							+ System.getProperty("line.separator");
					intent.putExtra("message", message);
					sendBroadcast(intent);
				}
				throw new InterruptedException();
			} catch (InterruptedException e) {
				intent.putExtra("exception", e.getMessage());
				intent.putExtra("message", "");
				sendBroadcast(intent);
			} catch (IOException e) {
				intent.putExtra("exception", e.getMessage());
				intent.putExtra("message", "");
				sendBroadcast(intent);
			}
		}
	}

	private boolean isConnectionAvailable() {
		return s.isConnected() || !s.isInputShutdown() || !s.isOutputShutdown();
	}

	private void sendMessage(final String command, final String tag) {
		try {
			if (isConnectionAvailable()) {
				// send output msg
				String outMsg = "msg:" + "Tag = " + tag + ", cmd = " + command;
				out.write(outMsg);
				out.flush();
			}
		} catch (UnknownHostException e) {
			intent.putExtra("exception", e.getMessage());
			intent.putExtra("message", "");
			sendBroadcast(intent);
		} catch (IOException e) {
			intent.putExtra("exception", e.getMessage());
			intent.putExtra("message", "");
			sendBroadcast(intent);
		}
	}

	@Override
	public void onResponse(int serviceId, Object responseObj) {
		if (responseObj != null && this != null) {
			switch (serviceId) {
			case SystemConstants.RESPONSE_SCANNING:
				if (responseObj != null) {

					ServerInfoModel object = (ServerInfoModel) responseObj;
					DashboardActivity.setCurrentItem(carService
							.getExecutedTasksCount() + "/254");
					if (object.isFound()) {
						DBServer model = new DBServer();
						model.setLast_time_seen(new Date().toString());
						model.setNetwork_type(object.getNetworkType());
						model.setNo_times_connected("1");
						model.setServer_ip(object.getServerIP());
						model.setServer_port(object.getServerPort());
						try {
							this.insertorUpdate(model);
						} catch (Exception e) {
							MyLog.e("Insertion Exception", e.getMessage());
						}
						byte[] blaBytes = CommonUtils.serializeObject(model);

						// DBServer deserializedBla = (DBServer)
						// CommonUtils.deserializeObject(blaBytes);
						intent.putExtra("server", blaBytes);
						sendBroadcast(intent);
					}
				}
				break;

			default:
				break;
			}
		}
	}

}
