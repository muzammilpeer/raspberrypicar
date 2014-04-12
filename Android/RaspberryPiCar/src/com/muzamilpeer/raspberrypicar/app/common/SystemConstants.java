package com.muzamilpeer.raspberrypicar.app.common;

import java.util.HashMap;

import com.muzamilpeer.raspberrypicar.model.ServerInfoModel;


public class SystemConstants {

	public static final String EMPTY_STRING = "";
	public static final String SHARED_SERVER_IP = "serverip";
	public static final String SHARED_SERVER_PORT = "port";
	
    public static final String SHARED_ALTER_PHONE_NO = "altPhoneNo";
    public static final String SHARED_FULL_NAME = "fullName";
    public static final String SHARED_IS_RETAILER = "isRetailer";
    public static final String SHARED_USER_NAME = "userName";
    public static final String SHARED_CNIC_NO = "cnicNo";
    public static final String SHARED_EMAIL = "email";
    public static final String SHARED_USER_TYPE = "userType";
    public static final String SHARED_FIRST_LOGIN = "firstLogin";
    public static final String SHARED_USER_ID = "userId";

    public static final String HEADER_SOURCE_ID = "ONELOAD";
    public static final String HEADER_CHANNEL_ID = "WEB";

    public static final String TX_AUTHENTICATION = "TX_AUTHENTICATION";
    public static final String TX_CHANGE_PASSWORD = "TX_CHANGE_PASSWORD";
    public static final String TX_CHANGE_PERSONAL_INFO = "TX_CHANGE_PERSONAL_INFO";
    public static final String TX_PROVIDER_LIST = "TX_PROVIDER_LIST";
    public static final String TX_BUY = "TX_BUY";
    public static final String TX_VIEW_BILL = "TX_VIEW_BILL";
    public static final String TX_BILL_PAYMENT = "TX_BILL_PAYMENT";
    public static final String TX_FUND_TRANSFER = "TX_FUND_TRANSFER";
    public static final String TX_TRANSACTION_HISTORY = "TX_TRANSACTION_HISTORY";
    public static final String TX_TRANSACTION_HISTORY_PAGINATED = "TX_TRANSACTION_HISTORY_PAGINATED";
    public static final String TX_CHECK_BALANCE = "TX_CHECK_BALANCE";
    public static final String TX_DENOMINATION_LIST = "TX_DENOMINATION_LIST";
    
    public static final String SERVER_LOOKUP_MESSAGE_SENT = "lookup:scan";
    public static final String SERVER_LOOKUP_MESSAGE_RECIEVED = "!raspberryPiCarHere!";

    public static boolean isIoT = false;

    public static final int SERVER_INTRANET_PORT = 80;
    public static final int SERVER_INTERNET_PORT = 80;
    
    public static String SERVER_INTRANET_IP_ADDRESS = "";
    public static String SERVER_INTERNET_IP_ADDRESS = "";
    
    public static String ANDROID_IP_ADDRESS = "";
    
    public static HashMap<String,ServerInfoModel> cacheServerListModel = new HashMap<String, ServerInfoModel>();
    
    public static final int RESPONSE_LOGIN = 1;
    public static final int RESPONSE_SCANNING = 2;
    public static final int RESPONSE_SCANNED = 3;
    public static final int RESPONSE_CONNECTED = 4;
    public static final int RESPONSE_DISCONNECTED = 5;
    public static final int RESPONSE_RECONNECT = 6;

}
