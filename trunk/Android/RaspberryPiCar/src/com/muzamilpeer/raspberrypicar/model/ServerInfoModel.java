package com.muzamilpeer.raspberrypicar.model;

public class ServerInfoModel {

	private String serverIP;
	private String serverPort;

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
