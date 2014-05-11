package com.muzamilpeer.raspberrypicar.dbmodel;

import java.io.Serializable;

public class DBServer  implements Serializable{
	public String server_ip;
	public String server_port;
	public String network_type;
	public String last_time_seen;
	public String no_times_connected;
	
	public String getLast_time_seen() {
		return last_time_seen;
	}
	
	public void setLast_time_seen(String last_time_seen) {
		this.last_time_seen = last_time_seen;
	}
	
	public String getNetwork_type() {
		return network_type;
	}
	public String getNo_times_connected() {
		return no_times_connected;
	}
	public String getServer_ip() {
		return server_ip;
	}
	
	public String getServer_port() {
		return server_port;
	}
	

	public void setNetwork_type(String network_type) {
		this.network_type = network_type;
	}
	
	public void setNo_times_connected(String no_times_connected) {
		this.no_times_connected = no_times_connected;
	}
	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}
	public void setServer_port(String server_port) {
		this.server_port = server_port;
	}
}
