package com.muzamilpeer.raspberrypicar.app.scan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.DashboardActivity;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.common.L;
import com.muzamilpeer.raspberrypicar.app.common.MyLog;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.app.manualdrive.ManualDriveFragment;
import com.muzamilpeer.raspberrypicar.dblayer.DBAccess;
import com.muzamilpeer.raspberrypicar.dblayer.DatabaseHelper;
import com.muzamilpeer.raspberrypicar.dblayer.DatabaseManager;
import com.muzamilpeer.raspberrypicar.dblayer.QueryExecutor;
import com.muzamilpeer.raspberrypicar.dbmodel.DBServer;
import com.muzamilpeer.raspberrypicar.model.ServerInfoModel;
import com.muzamilpeer.raspberrypicar.networklayer.RaspberryPiNetworkService;
import com.muzamilpeer.raspberrypicar.networklayer.ResponseListener;

public class ScanCarFragment extends SherlockFragment implements
		ResponseListener {
	
	Context context;
	View mainView;
	ActionBar actionBar;
	CommonActions ca;
	ArrayList<Object> listProviderArray = new ArrayList<Object>();

	TextView tvScanStatus,tvServerList;
	RaspberryPiNetworkService carService;
	ProgressBar pbServerList;

	ListView serverListView;
	ArrayList<ServerInfoModel> serverListDataSource;
	CarsListViewAdapter serverListAdaptor;
	
	private void getServersfromCache() {
		serverListDataSource.clear();
		Iterator<Entry<String, ServerInfoModel>> iter = SystemConstants.cacheServerListModel.entrySet().iterator();
		 
		while (iter.hasNext()) {
			Map.Entry<String, ServerInfoModel> mEntry = (Map.Entry<String, ServerInfoModel>) iter.next();
			serverListDataSource.add(mEntry.getValue());
		}
		serverListAdaptor.notifyDataSetChanged();
	}
	protected void fetchRecord() throws Exception {
		L.LOG_TAG = "database";
		DatabaseManager.initializeInstance(new DatabaseHelper(context));

		L.e("Select  record");

		DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
			@Override
			public void run(SQLiteDatabase database) {
				
				DBServer model = new DBServer();

				Map<String, String> map = new HashMap<String, String>();
				
				
				ArrayList<Object> response =  new DBAccess(database, context)
						.selectAll(map, model);
				
				for (int i = 0; i < response.size(); i++) {
					DBServer obj = (DBServer)response.get(i);
					ServerInfoModel modelObject = new ServerInfoModel();
					modelObject.setExceptionMessage("");
					modelObject.setFound(true);
					modelObject.setNetworkType("net");
					modelObject.setServerIP(obj.getServer_ip());
					modelObject.setServerPort(obj.getServer_port());
					modelObject.setTaskId(0);
					serverListDataSource.add(modelObject);
				}
//				serverListAdaptor.notifyDataSetChanged();
				L.e("Select response"+ response);
			}
		});
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		mainView = inflater.inflate(R.layout.activity_scancar, null);

			initViews();
			initObjects();
			initListeners();
			try {
				fetchRecord();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				MyLog.e("Error in Fetching", e.getMessage());
			}			
			
//			Log.e("First time", "First Time here ")	;
//			if(SystemConstants.cacheServerListModel.size() > 0)  {
//				getServersfromCache();
//				
//			}
		return mainView;
	}

	private void initViews() {
		tvScanStatus = (TextView) mainView.findViewById(R.id.tvScanStatus);
		tvServerList = (TextView) mainView.findViewById(R.id.tvServerList);
		pbServerList = (ProgressBar) mainView.findViewById(R.id.pbServerList);
		serverListView = (ListView) mainView
				.findViewById(R.id.lvRaspberryPiCars);

	}

	private void initListeners() {
		//tvScanStatus.setOnClickListener(mGlobal_OnClickListener);
		DashboardActivity.setRefreshButtonListener(mGlobal_OnClickListener);

		serverListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 //Load Respective Screen on Main Fragment View
				 //ca.saveUserPreferences("selectedServer",
				ServerInfoModel model =  serverListDataSource.get(position);
				if(model.isFound()) {
					ca.saveUserPreferences(SystemConstants.SHARED_SERVER_IP, model.getServerIP());
					ca.saveUserPreferences(SystemConstants.SHARED_SERVER_PORT, model.getServerPort());
					
					DashboardActivity.refreshMainViewByNew(new ManualDriveFragment());
				}else {
					Toast.makeText(getSherlockActivity(), "No connection available at this machine", 1).show();
					
				}
		
			}
		});

	}
	

	private void initObjects() {
		this.context = getSherlockActivity().getApplicationContext();
		this.carService = new RaspberryPiNetworkService(getActivity());
		this.carService.setResponseListener(this);
		
		serverListDataSource = new ArrayList<ServerInfoModel>();

		// ListView Adaptor
		serverListAdaptor = new CarsListViewAdapter(mainView.getContext(),
				R.layout.cars_row, serverListDataSource);
		serverListView.setAdapter(serverListAdaptor);

		// Common View
		ca = new CommonActions(getActivity());
		
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
			case R.id.btnRefresh: {
				 * ActionBar actionBar = getSherlockActivity()
				 * .getSupportActionBar(); if (actionBar.isShowing()) {
				 * actionBar.hide();
				 * 
				 * } else { actionBar.show(); }
				serverListDataSource.clear();
				serverListAdaptor.notifyDataSetChanged();
				
				carService.callScanningService();

				 * setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
				 * );
			}
							 */

			case R.id.btnRefreshMenu: {
				serverListDataSource.clear();
				SystemConstants.cacheServerListModel.clear();
				serverListAdaptor.notifyDataSetChanged();
				tvScanStatus.setText("Scanning");
				
				pbServerList.setVisibility(View.VISIBLE);
				tvServerList.setText("Scanning");
				tvServerList.setVisibility(View.VISIBLE);
				serverListView.setVisibility(View.GONE);
				if(!carService.isRunning()) {
					carService.callScanningService();
				}else {
					tvScanStatus.setText("Applicaiton is already scanning Please wait...");
				}
			}
			
				break;
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

	@Override
	public void onResponse(int serviceId, Object responseObj) {
		if (responseObj != null && this != null) {
			switch (serviceId) {
			case SystemConstants.RESPONSE_SCANNING:
				if (responseObj != null) {

					ServerInfoModel object = (ServerInfoModel) responseObj;
					DashboardActivity.setCurrentItem(carService.getExecutedTasksCount()+"/254");
					if(object.isFound()) {
						pbServerList.setVisibility(View.GONE);
						tvServerList.setText("Found");
						tvServerList.setVisibility(View.GONE);
						serverListView.setVisibility(View.VISIBLE);

						serverListDataSource.add(object);
						SystemConstants.cacheServerListModel.put(object.getServerIP(), object);
						// SystemConstants.cacheServerListModel.add((ServerInfoModel)
						// responseObj);
						serverListAdaptor.notifyDataSetChanged();
					}
					if(object.getTaskId()  == 254) {
						tvScanStatus.setText("Done");
						DashboardActivity.hideLoader();
						carService.stopScanning();
						if(serverListDataSource.size() < 1) {
							pbServerList.setVisibility(View.GONE);
							tvServerList.setText("No Server Found");
							tvServerList.setVisibility(View.VISIBLE);
							serverListView.setVisibility(View.GONE);
						}
					}
				}
				break;

			default:
				break;
			}
		}
	}
}
