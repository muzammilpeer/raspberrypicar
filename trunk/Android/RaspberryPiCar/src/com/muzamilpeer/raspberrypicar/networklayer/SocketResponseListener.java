package com.muzamilpeer.raspberrypicar.networklayer;



public interface SocketResponseListener {
    public void onSocketResponse(int serviceId,Object responseObj);
}
