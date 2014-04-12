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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.Log;

import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.model.ServerInfoModel;
import com.muzamilpeer.raspberrypicar.model.ServerListModel;

public class CarScanningService {
	private Vector<ServerInfoModel> listofServers ;
	private Collection<ServerInfoModel> listofServersCollection ;
	private AtomicInteger executedTasksCount;
	
    private static ExecutorService SINGLE_TASK_EXECUTOR;
    private static ExecutorService LIMITED_TASK_EXECUTOR;
    private static ExecutorService FULL_TASK_EXECUTOR;
    
    static {
        SINGLE_TASK_EXECUTOR = (ExecutorService) Executors.newSingleThreadExecutor();
        LIMITED_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(7);
        FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
    };
    
	private Context context;
	
	public CarScanningService(Context context) {
		this.context = context;
		this.executedTasksCount = new AtomicInteger(0); // reset this count
		this.listofServers = new Vector<ServerInfoModel>();
		this.listofServersCollection = Collections.synchronizedCollection(new Vector<ServerInfoModel>());
	}
	
	public boolean isWifiAvailable() {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiMgr.isWifiEnabled();
	}
	public String getWifiIPAddress() {
		if(isWifiAvailable()) {
	        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
	        int ip = wifiInfo.getIpAddress();
	        String ipAddress = Formatter.formatIpAddress(ip);
	        return ipAddress;
		}else  {
			//local ip address
			return "127.0.0.1";
		}
	}
	
   private void startTask(int taskId, String ipaddress,int portNumber) {
		TestTask task = new TestTask(taskId, ipaddress,portNumber);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		 if (currentapiVersion >=  android.os.Build.VERSION_CODES.HONEYCOMB) {
	 		task.executeOnExecutor(SINGLE_TASK_EXECUTOR);
		 } else {
		      task.execute();
		 }
   }
	   
    private class TestTask extends AsyncTask<Void, Void, Void> /* Params, Progress, Result */ {

        private final int id;
        private final int portNumber;
        private final String nextIpAddress;
        
        TestTask(int id, String ipaddress,int portNumber) {
            this.id = id;
            this.portNumber = portNumber;
            this.nextIpAddress = ipaddress;
            
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            int taskExecutionNumber = executedTasksCount.incrementAndGet();
            log("doInBackground: entered, taskExecutionNumber = " + taskExecutionNumber);
            //String nextIpAddress
    	    PrintWriter out = null;
    	    BufferedReader in = null;
            try {
	        	//Log.e("Connecting Port","Connecting to "+ this.nextIpAddress);
	            Socket mySocket = new Socket();
				SocketAddress address = new InetSocketAddress(this.nextIpAddress, this.portNumber);
				//timeout 5 miliseconds
	            mySocket.connect(address, 5);

	            out = new PrintWriter(mySocket.getOutputStream(), true);
	            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
	            
	            //Send Lookup message to server
	            out.write(SystemConstants.SERVER_LOOKUP_MESSAGE_SENT);
	            out.flush();
//	            SystemClock.sleep(500); 
	            
	            String fromServer;
	            while ((fromServer = in.readLine()) != null) {
	                if (fromServer.equals(SystemConstants.SERVER_LOOKUP_MESSAGE_RECIEVED)) {
	                	Log.e("Server Found at ",this.nextIpAddress);
	                    synchronized(listofServersCollection) {
	                    	ServerInfoModel model = new ServerInfoModel();
	                    	model.setServerIP(this.nextIpAddress);
	                    	model.setServerPort(String.valueOf(portNumber));
	                    	listofServersCollection.add(model);
	                    }             
	                	
//		                    servers.add(iIPv4 + i);
	                    mySocket.close();
	                    break;
	                }
	            }
	            mySocket.close();
	            out.close();
	            in.close();

	        } catch (UnknownHostException e) {
	        	//Log.e("Unknown Host Exception ", e.getMessage());
	        } catch (IOException e) {
	        	//Log.e("IOException", e.getMessage());
	        }	            
           // log("doInBackground: is about to finish, taskExecutionNumber = " + taskExecutionNumber);
            return null;
        }
        
        private void log(String msg) {
           // Log.e("TestTask #" + id, msg);
        }
    }	   
	   
	public  ServerListModel serviceScanner()  {
		ArrayList<ServerInfoModel> models = new ArrayList<ServerInfoModel>();
		if(isWifiAvailable()) {
		    // Get the IP of the local machine
		    String iIPv4 = "";
	        iIPv4 = getWifiIPAddress();
	        SystemConstants.ANDROID_IP_ADDRESS = iIPv4;
		    iIPv4 = iIPv4.substring(0, iIPv4.lastIndexOf("."));
		    iIPv4 += ".";
		    int portNumber = SystemConstants.isIoT? SystemConstants.SERVER_INTERNET_PORT : SystemConstants.SERVER_INTRANET_PORT;
		    // Loop to scan each address on the local subnet
		    for (int i = 1; i < 255; i++) {
	        	//Log.i("Connecting Port","Connecting to "+ iIPv4+i+"");
		        this.startTask(i, iIPv4+i, portNumber);
		    }
		    //Wait for scan result to get result and then move to next line of code...else your list will be empty due to thread scheduling
		    SystemClock.sleep(5000); 
		    //Object[] serversArray = this.listofServersCollection;
		    synchronized (this.listofServersCollection) {
			    Iterator<ServerInfoModel> iterator = this.listofServersCollection.iterator();
				while (iterator.hasNext()) {
					models.add(iterator.next());
				}	
		    }
		}else {
			ServerInfoModel obj = new ServerInfoModel();
			obj.setServerIP("No Record Found");
			obj.setServerPort("");
			models.add(obj);
		}
		
	    ServerListModel model = new ServerListModel();
	    model.setServers(models);
	    
	    return model;
	}
	
	

}
