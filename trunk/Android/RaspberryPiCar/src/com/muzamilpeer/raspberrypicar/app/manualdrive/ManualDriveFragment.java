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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;

public class ManualDriveFragment extends SherlockFragment implements
		OnTouchListener {

	View mainView;
	ActionBar actionBar;
	CommonActions ca;
	ArrayList<Object> listProviderArray = new ArrayList<Object>();

	Socket s;
	BufferedReader in;
	BufferedWriter out;
	ImageButton btnForward, btnReverse, btnLeft, btnRight, btnLeftIndicator,
			btnRightIndicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		mainView = inflater.inflate(R.layout.activity_manualdrive, null);

		if (savedInstanceState == null) {
			initViews();
			initObjects();
			initListeners();
			// getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		return mainView;
	}

	private void initViews() {

		btnForward = (ImageButton) mainView.findViewById(R.id.btnForward);
		btnReverse = (ImageButton) mainView.findViewById(R.id.btnReverse);
		btnLeft = (ImageButton) mainView.findViewById(R.id.btnLeft);
		btnRight = (ImageButton) mainView.findViewById(R.id.btnRight);
		btnLeftIndicator = (ImageButton) mainView
				.findViewById(R.id.btnLeftIndicator);
		btnRightIndicator = (ImageButton) mainView
				.findViewById(R.id.btnRightIndicator);

	}

	private void initListeners() {
		btnForward.setOnTouchListener(this);
		btnReverse.setOnTouchListener(this);
		btnLeft.setOnTouchListener(this);
		btnRight.setOnTouchListener(this);
		btnLeftIndicator.setOnTouchListener(this);
		btnRightIndicator.setOnTouchListener(this);
	}

	public void connectToServer() {

		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
			};

			@Override
			protected Void doInBackground(Void... params) {
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
						String iam = "iam:androidphone";
						out.write(iam);
						out.flush();
					} else {
						in = new BufferedReader(new InputStreamReader(
								s.getInputStream()));
						out = new BufferedWriter(new OutputStreamWriter(
								s.getOutputStream()));
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			}

		}.execute();
	}

	private void initObjects() {
		// Common View
		ca = new CommonActions(getActivity());
		connectToServer();

	}

	private boolean validation() {
		boolean isValid = true;
		return isValid;
	}

	// Global On click listener for all views
	final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		public void onClick(final View v) {
			switch (v.getId()) {
			/*
			 * case R.id.btnBuy: //Buy Button Pressed call Buy Services and show
			 * confirmation dialog //showBuyVoucherConfirmationDialog(); break;
			 */
			}
		}
	};

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isNew", false);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	public void sendCommand(final View view, final String command) {

		
		new AsyncTask<Void, Void, String>() {

			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(Void... params) {
				try {
					// send output msg
					String outMsg = "msg:" + "Tag = " + view.getTag() + ", cmd = "
							+ command;
					// String outMsg = "msg:"+view.getTag() + ","+command;
					out.write(outMsg);
					out.flush();
					Log.i("RcCarController", "sent: " + outMsg);
					// accept server response
					String inMsg = in.readLine() + System.getProperty("line.separator");
					Log.i("RcCarController", "received: " + inMsg);
					return inMsg;
					// s.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				TextView tv;
				tv = (TextView) mainView.findViewById(R.id.txtStatus);
				tv.setText(result);

			}

		}.execute();		
	}

	// see
	// http://developer.android.com/guide/topics/ui/ui-events.html#EventListeners
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int eventaction = event.getAction();

		switch (eventaction) {
		case MotionEvent.ACTION_DOWN:
			// finger touches the screen
			sendCommand(v, "down");
			Log.e("RcCarController", "down");
			break;

		case MotionEvent.ACTION_MOVE:
			// finger moves on the screen
			sendCommand(v, "move");
			Log.e("RcCarController", "move");
			break;

		case MotionEvent.ACTION_UP:
			// finger leaves the screen
			sendCommand(v, "up");
			Log.e("RcCarController", "up");
			break;
		}
		return false;
	}
}