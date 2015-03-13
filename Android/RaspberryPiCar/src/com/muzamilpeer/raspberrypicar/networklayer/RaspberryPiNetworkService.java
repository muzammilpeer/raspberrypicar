package com.muzamilpeer.raspberrypicar.networklayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;

import com.muzamilpeer.raspberrypicar.app.DashboardActivity;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.common.MyLog;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.model.ServerInfoModel;

public class RaspberryPiNetworkService {
	Context context;
	CommonActions ca;
	ResponseListener responseListner;
	private boolean isRunning = false;

	private AtomicInteger executedTasksCount;
	private ArrayList<SingleScanTask> arrayofThreads;

	public AtomicInteger getExecutedTasksCount() {
		return this.executedTasksCount;
	}

	private static ExecutorService SINGLE_TASK_EXECUTOR;
	private static ExecutorService LIMITED_TASK_EXECUTOR;
	private static ExecutorService FULL_TASK_EXECUTOR;

	static {
		SINGLE_TASK_EXECUTOR = (ExecutorService) Executors
				.newSingleThreadExecutor();
		LIMITED_TASK_EXECUTOR = (ExecutorService) Executors
				.newFixedThreadPool(7);
		FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	};

	public RaspberryPiNetworkService(Context context) {
		super();
		this.context = context;
		this.ca = new CommonActions(context);
		this.executedTasksCount = new AtomicInteger(0); // reset this count
	}

	public void setResponseListener(ResponseListener listener) {
		this.responseListner = listener;
	}

	// /////////

	public boolean isWifiAvailable() {
		WifiManager wifiMgr = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wifiMgr.isWifiEnabled();
	}

	public String getWifiIPAddress() {
		if (isWifiAvailable()) {
			WifiManager wifiMgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
			int ip = wifiInfo.getIpAddress();
			String ipAddress = Formatter.formatIpAddress(ip);
			return ipAddress;
		} else {
			// local ip address
			return "127.0.0.1";
		}
	}
	public boolean isRunning() {
		return isRunning;
	}
	
	public void stopScanning() {
		if(isRunning) {
			isRunning = false;
		}
	}

	private void scanNetworkTask() {
		this.executedTasksCount = new AtomicInteger(0);
		this.isRunning = true;
		// this.arrayofThreads = new
		// ArrayList<RaspberryPiNetworkService.SingleScanTask>();

		if (isWifiAvailable()) {
			// Get the IP of the local machine
			String iIPv4 = "";
			iIPv4 = getWifiIPAddress();
			SystemConstants.ANDROID_IP_ADDRESS = iIPv4;
			iIPv4 = iIPv4.substring(0, iIPv4.lastIndexOf("."));
			iIPv4 += ".";
			String networkType = SystemConstants.isIoT ? "Internet"
					: "Intranet";
			int portNumber = SystemConstants.isIoT ? SystemConstants.SERVER_INTERNET_PORT
					: SystemConstants.SERVER_INTRANET_PORT;
			// Loop to scan each address on the local subnet
			for (int i = 1; i < 255; i++) {
				// Log.i("Connecting Port","Connecting to "+ iIPv4+i+"");
				this.startTask(i, iIPv4 + i, portNumber, networkType);
			}
		}
	}

	private void startTask(int taskId, String ipaddress, int portNumber,
			String networkType) {
		SingleScanTask task = new SingleScanTask(taskId, ipaddress, portNumber,
				networkType);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(SINGLE_TASK_EXECUTOR);
		} else {
			task.execute();
		}
	}

	private class SingleScanTask extends AsyncTask<Void, Void, ServerInfoModel> {

		private final int portNumber;
		private final String nextIpAddress;
		private final int id;
		private final String networkType;

		SingleScanTask(int taskid, String ipaddress, int portNumber,
				String networkType) {
			this.portNumber = portNumber;
			this.nextIpAddress = ipaddress;
			this.id = taskid;
			this.networkType = networkType;
		}

		protected void onPreExecute() {

		};

		@Override
		protected ServerInfoModel doInBackground(Void... params) {

			if (isRunning) {

				int taskExecutionNumber = executedTasksCount.incrementAndGet();
				MyLog.i("doInBackground", "taskExecutionNumber = "
						+ taskExecutionNumber);

				PrintWriter out = null;
				BufferedReader in = null;
				ServerInfoModel model = new ServerInfoModel();
				model.setNetworkType(networkType);
				model.setServerIP(this.nextIpAddress);
				model.setServerPort(String.valueOf(portNumber));
				model.setFound(false);
				model.setTaskId(taskExecutionNumber);

				try {
					Socket mySocket = new Socket();
					SocketAddress address = new InetSocketAddress(
							this.nextIpAddress, this.portNumber);
					mySocket.connect(address, 5);

					out = new PrintWriter(mySocket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(
							mySocket.getInputStream()));

					// Send Lookup message to server
					out.write(SystemConstants.SERVER_LOOKUP_MESSAGE_SENT);
					out.flush();

					String fromServer;
					while ((fromServer = in.readLine()) != null) {
						if (fromServer
								.equals(SystemConstants.SERVER_LOOKUP_MESSAGE_RECIEVED)) {
							MyLog.e("Server Found at ", this.nextIpAddress);
							model.setFound(true);
						}
						mySocket.close();
						break;
					}
					mySocket.close();
					out.close();
					in.close();

				} catch (UnknownHostException e) {
					MyLog.e("Unknown Host Exception ", e.getMessage());
					model.setExceptionMessage(e.getMessage());
				} catch (IOException e) {
					MyLog.e("IOException", e.getMessage());
					model.setExceptionMessage(e.getMessage());
				}
				return model;
			} else {
				this.cancel(true);
				return null;
			}

		}

		@Override
		protected void onPostExecute(ServerInfoModel result) {
			super.onPostExecute(result);
			responseListner.onResponse(SystemConstants.RESPONSE_SCANNING,
					result);
		}
	}

	public void callScanningService() {
		new AsyncTask<Void, Void, Void>() {
			protected void onPreExecute() {
				DashboardActivity.showLoader();
			};

			@Override
			protected Void doInBackground(Void... params) {
				try {
					scanNetworkTask();
					/*
					 * for (int i = 0; i < model.getServers().size(); i++) {
					 * MyLog.e("Server after search", model.getServers().get(i)
					 * .getServerIP()); }
					 */
					return null;
				} catch (Exception e) {
					MyLog.e("RaspberryPiCar Exception : ", e.getMessage());
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				ca.hideProgress();
				responseListner.onResponse(SystemConstants.RESPONSE_SCANNED,
						result);
			}
		}.execute();
	}

}
