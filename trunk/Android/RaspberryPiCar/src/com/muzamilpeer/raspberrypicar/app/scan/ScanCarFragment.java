package com.muzamilpeer.raspberrypicar.app.scan;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.model.ServerInfoModel;
import com.muzamilpeer.raspberrypicar.model.ServerListModel;
import com.muzamilpeer.raspberrypicar.networklayer.RaspberryPiNetworkService;
import com.muzamilpeer.raspberrypicar.networklayer.ResponseListener;

public class ScanCarFragment extends SherlockFragment implements ResponseListener {

    View mainView;
    ActionBar actionBar;
    CommonActions ca;
	ArrayList<Object> listProviderArray = new ArrayList<Object>();
	
	Button btnRefresh;
	RaspberryPiNetworkService carService;
	
    ListView serverListView;
    ArrayList<ServerInfoModel> serverListDataSource;
    CarsListViewAdapter serverListAdaptor;


	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        
        mainView = inflater.inflate(R.layout.activity_scancar, null);
        

        if (savedInstanceState == null) {
            initViews();
            initObjects();
            initListeners();
        	this.carService.callScanningService();
           // getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        return mainView;
    }
    private void initViews() {
    	btnRefresh = (Button) mainView.findViewById(R.id.btnRefresh);
    	serverListView = (ListView)mainView.findViewById(R.id.lvRaspberryPiCars);

    	
    }
    private void initListeners() {
    	btnRefresh.setOnClickListener(mGlobal_OnClickListener);
    	serverListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    //Load Respective Screen on Main Fragment View
				//ca.saveUserPreferences("selectedServer", serverListDataSource.get(position));
			  //  Intent intent = new Intent(ScanNetwork.this, MainControllerActivity.class);
			  //  startActivity(intent);
			}
		});    	  

    }

    private void initObjects() {
    	this.carService = new RaspberryPiNetworkService(getActivity());
    	this.carService.setResponseListener(this);

    	serverListDataSource = new ArrayList<ServerInfoModel>();
        
		//ListView Adaptor
    	serverListAdaptor = new CarsListViewAdapter(mainView.getContext(), R.layout.cars_row, serverListDataSource);
		serverListView.setAdapter(serverListAdaptor);

    	//Common View 
    	ca = new CommonActions(getActivity());
    }
    
    private boolean validation()
    {
    	boolean isValid = true;
    	return isValid;
    }
    //Global On click listener for all views
    final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btnRefresh:
                {
                    ActionBar actionBar = getSherlockActivity().getSupportActionBar();
                    if(actionBar.isShowing()) {
                        actionBar.hide();
                    	
                    }else {
                        actionBar.show();
                    }
                    
                    /*
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                     * */
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
	       switch (serviceId) {

	        case SystemConstants.RESPONSE_SCANNING:
	            if (responseObj != null) {
	            	SystemConstants.cacheServerListModel = (ServerListModel)responseObj;
	            	serverListDataSource.clear();
	            	serverListDataSource.addAll(((ServerListModel)responseObj).getServers());
	               	serverListAdaptor.notifyDataSetChanged();
	            }
	        break;
	        
	        default:
            break;
	    }
	}
}
