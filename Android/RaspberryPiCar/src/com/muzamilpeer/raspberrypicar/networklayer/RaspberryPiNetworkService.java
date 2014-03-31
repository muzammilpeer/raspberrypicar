package com.muzamilpeer.raspberrypicar.networklayer;


import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.model.ServerInfoModel;
import com.muzamilpeer.raspberrypicar.model.ServerListModel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RaspberryPiNetworkService {
    Context context;
    CommonActions ca;
    ResponseListener responseListner;

    public RaspberryPiNetworkService(Context context) {
    	super();
        this.context = context;
        this.ca = new CommonActions(context);
    }

    public void setResponseListener(ResponseListener listener) {
        this.responseListner = listener;
    }

    public void callScanningService() {
        new AsyncTask<Void, Void, ServerListModel>() {
            protected void onPreExecute() {
                ca.showProgress();
            };

            @Override
            protected ServerListModel doInBackground(Void... params) {
                try {
                	ServerListModel model = new ServerListModel();
                	CarScanningService service = new CarScanningService(context);
                	model = service.serviceScanner();
                	for (int i = 0; i < model.getServers().size(); i++) {
						Log.e("Server after search", model.getServers().get(i).getServerIP());
					}
                	 
                    return model;
                } catch (Exception e) {
                	Log.e("RaspberryPiCar Exception : ", e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(ServerListModel result) {
                super.onPostExecute(result);
                ca.hideProgress();
                responseListner.onResponse(SystemConstants.RESPONSE_SCANNING, result);
            }
        }.execute();
    }
    
    
}
