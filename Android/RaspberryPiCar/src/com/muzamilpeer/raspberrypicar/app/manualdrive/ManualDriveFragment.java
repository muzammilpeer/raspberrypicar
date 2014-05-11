package com.muzamilpeer.raspberrypicar.app.manualdrive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.DashboardActivity;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.common.MyLog;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.app.scan.ScanCarFragment;

public class ManualDriveFragment extends SherlockFragment implements
		OnTouchListener {

	View mainView;
	ActionBar actionBar;
	CommonActions ca;
	ArrayList<Object> listProviderArray = new ArrayList<Object>();

	private boolean isConnected = false;
	Socket s;
	BufferedReader in;
	BufferedWriter out;
	ImageButton btnForward, btnReverse, btnLeft, btnRight, btnLeftIndicator,
			btnRightIndicator, btnConnectDisconnect;
	
	Button btnShutdown;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		mainView = inflater.inflate(R.layout.activity_manualdrive, null);

		if (savedInstanceState == null) {
			initViews();
		}
		//initObjects();
		//initListeners();

		return mainView;
	}

	private void initViews() {

		btnForward = (ImageButton) mainView.findViewById(R.id.btnForward);
		btnReverse = (ImageButton) mainView.findViewById(R.id.btnReverse);
		btnLeft = (ImageButton) mainView.findViewById(R.id.btnLeft);
		btnRight = (ImageButton) mainView.findViewById(R.id.btnRight);
		btnConnectDisconnect = (ImageButton) mainView
				.findViewById(R.id.btnConnectDisconnect);
		btnLeftIndicator = (ImageButton) mainView
				.findViewById(R.id.btnLeftIndicator);
		btnRightIndicator = (ImageButton) mainView
				.findViewById(R.id.btnRightIndicator);
		
		btnShutdown =  (Button) mainView.findViewById(R.id.btnShutdown);

	}

	private void initListeners() {
		btnForward.setOnTouchListener(this);
		btnReverse.setOnTouchListener(this);
		btnLeft.setOnTouchListener(this);
		btnRight.setOnTouchListener(this);
		btnLeftIndicator.setOnTouchListener(this);
		btnRightIndicator.setOnTouchListener(this);
		btnConnectDisconnect.setOnClickListener(mGlobal_OnClickListener);
		btnShutdown.setOnClickListener(mGlobal_OnClickListener);
	}

	private void initObjects() {
		// Common View
		ca = new CommonActions(getActivity());
		if (isSharedPreferencesAvailable()) {
			btnConnectDisconnect
					.setImageResource(android.R.drawable.presence_offline);
			connectToServer();
			//liveServerStatus();
		} else {
			DashboardActivity.refreshMainViewByNew(new ScanCarFragment());
			btnConnectDisconnect
					.setImageResource(android.R.drawable.presence_offline);
		}
	}

	private String disconnectClient() {
		btnConnectDisconnect
				.setImageResource(android.R.drawable.presence_offline);

		isConnected = false;
		try {
			in.close();
			in = null;
			out.close();
			out = null;
			s.close();
			s = null;
		} catch (IOException e) {
			in = null;
			out = null;
			s = null;
			MyLog.e("IOException", e.getMessage());
			return e.getMessage();
		}
		return null;
	}

	// Global On click listener for all views
	final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		public void onClick(final View v) {
			switch (v.getId()) {
			case R.id.btnConnectDisconnect: {
				if (isSharedPreferencesAvailable()) {
					btnConnectDisconnect
							.setImageResource(android.R.drawable.presence_offline);
					if (!isConnected) {
						connectToServer();
						//liveServerStatus();
					} else {
						disconnectClient();
					}
				} else {
					btnConnectDisconnect
							.setImageResource(android.R.drawable.presence_offline);
				}
				break;
			}
			case R.id.btnShutdown: {
				shutdownServer();
				break;
			}
			
			}
		}
	};

	private boolean isSharedPreferencesAvailable() {
		if (ca.getValueString(SystemConstants.SHARED_SERVER_IP, null) != null)
			return true;
		else
			return false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isNew", false);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	// see
	// http://developer.android.com/guide/topics/ui/ui-events.html#EventListeners
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isConnected) {

			int eventaction = event.getAction();

			switch (eventaction) {
			case MotionEvent.ACTION_DOWN:
				// finger touches the screen
				sendCommand(v, "down");
				Log.e("RcCarController", "down");
				break;

			case MotionEvent.ACTION_MOVE:
				// finger moves on the screen
				//sendCommand(v, "move");
				//Log.e("RcCarController", "move");
				break;

			case MotionEvent.ACTION_UP:
				// finger leaves the screen
				sendCommand(v, "up");
				Log.e("RcCarController", "up");
				break;
			}
		}
		return false;
	}

	public void connectToServer() {

		new AsyncTask<Void, Void, String>() {
			private String exception = "";
			protected void onPreExecute() {
			};

			private String connectMessage() {
				String inMsg = "";
				try {
					String iam = "iam:androidphone";
					out.write(iam);
					out.flush();
					inMsg = in.readLine()
							+ System.getProperty("line.separator");
				} catch (IOException e) {
					MyLog.e("IOException", e.getMessage());
				}
				MyLog.e("data from server", inMsg);
				// androidphone has joined
				return inMsg;
			}

			@Override
			protected String doInBackground(Void... params) {
				try {
					if (s == null) {
						s = new Socket(ca.getValueString(
								SystemConstants.SHARED_SERVER_IP, null),
								Integer.parseInt(ca.getValueString(
										SystemConstants.SHARED_SERVER_PORT,
										null)));
						in = new BufferedReader(new InputStreamReader(
								s.getInputStream()));
						out = new BufferedWriter(new OutputStreamWriter(
								s.getOutputStream()));

						return connectMessage();
					} else {
						in = new BufferedReader(new InputStreamReader(
								s.getInputStream()));
						out = new BufferedWriter(new OutputStreamWriter(
								s.getOutputStream()));
						return connectMessage();
					}
				} catch (UnknownHostException e) {
					exception = e.getMessage();
					MyLog.e("UnknownHostException", exception);
				} catch (IOException e) {
					exception = e.getMessage();
					MyLog.e("IOException", exception);
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (result != null) {
					btnConnectDisconnect
							.setImageResource(android.R.drawable.presence_online);
					isConnected = true;
				}else {
					CommonActions.showAlert(getSherlockActivity(),
							"Connection Exception", exception);
					btnConnectDisconnect.setImageResource(android.R.drawable.presence_offline);
					isConnected = false;
				}
			}

		}.execute();
	}
	
	
	public void shutdownServer() {

		new AsyncTask<Void, Void, String>() {
			private String exception = null;
			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(Void... params) {
				try {
					// send output msg
					String outMsg = "shutdown:shutdown";
					// String outMsg = "msg:"+view.getTag() + ","+command;
					out.write(outMsg);
					out.flush();
					Log.i("RcCarController", "sent: " + outMsg);
					// accept server response
					String inMsg = in.readLine()
							+ System.getProperty("line.separator");
					Log.i("RcCarController", "received: " + inMsg);
					return inMsg;
					// s.close();
				} catch (UnknownHostException e) {
					exception = e.getMessage();
					MyLog.e("UnknownHostException", exception);
					return e.getMessage();
				} catch (IOException e) {
					exception = e.getMessage();
					MyLog.e("IOException", exception);
					return e.getMessage();
				}
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				TextView tv = (TextView) mainView.findViewById(R.id.txtStatus);
				tv.setText(result);
				if(exception != null) {
					CommonActions.showAlert(getSherlockActivity(),
							"IOException", exception);
					btnConnectDisconnect
					.setImageResource(android.R.drawable.presence_offline);
					isConnected = false;
				}
			}

		}.execute();
	}
	
	public void sendCommand(final View view, final String command) {

		new AsyncTask<Void, Void, String>() {
			private String exception = null;
			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(Void... params) {
				try {
					// send output msg
					String outMsg = "msg:" + "Tag = " + view.getTag()
							+ ", cmd = " + command;
					// String outMsg = "msg:"+view.getTag() + ","+command;
					out.write(outMsg);
					out.flush();
					Log.i("RcCarController", "sent: " + outMsg);
					// accept server response
					String inMsg = in.readLine()
							+ System.getProperty("line.separator");
					Log.i("RcCarController", "received: " + inMsg);
					return inMsg;
					// s.close();
				} catch (UnknownHostException e) {
					exception = e.getMessage();
					MyLog.e("UnknownHostException", exception);
					return e.getMessage();
				} catch (IOException e) {
					exception = e.getMessage();
					MyLog.e("IOException", exception);
					return e.getMessage();
				}
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				TextView tv = (TextView) mainView.findViewById(R.id.txtStatus);
				tv.setText(result);
				if(exception != null) {
					CommonActions.showAlert(getSherlockActivity(),
							"IOException", exception);
					btnConnectDisconnect
					.setImageResource(android.R.drawable.presence_offline);
					isConnected = false;
				}
			}

		}.execute();
	}
	
	public void liveServerStatus() {

		new AsyncTask<Void, Void, String>() {
			private String exception = null;
			protected void onPreExecute() {
			};

		
			@Override
			protected String doInBackground(Void... params) {
				while(true) {
					if(s != null && isConnected) {
						if(!s.isConnected() || s.isInputShutdown() || s.isOutputShutdown()) {
							exception = "disconnected";
							return exception;
						}
					}
					SystemClock.sleep(2000);
				}
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (result != null) {
					btnConnectDisconnect
							.setImageResource(android.R.drawable.presence_offline);
					isConnected = false;
				}
			}

		}.execute();
	}

}