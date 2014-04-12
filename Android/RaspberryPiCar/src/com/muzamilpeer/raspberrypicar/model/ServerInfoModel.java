package com.muzamilpeer.raspberrypicar.model;

public class ServerInfoModel {
	
	private String exceptionMessage;
	private String serverIP;
	private String serverPort;
	private String networkType;
	private boolean found;
	private int taskId;
	
	public int getTaskId() {
		return taskId;
	}
	
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	public boolean isFound() {
		return found;
	}
	public void setFound(boolean found) {
		this.found = found;
	}
	
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getServerIP() {
		return serverIP;
	}
	
	public String getServerPort() {
		return serverPort;
	}
	
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	} 
	
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
}
