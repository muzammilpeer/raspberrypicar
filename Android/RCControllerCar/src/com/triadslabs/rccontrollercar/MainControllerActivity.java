/**
 * 
 */
package com.triadslabs.rccontrollercar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author root
 *
 */
public class MainControllerActivity extends Activity implements OnTouchListener{
	Socket s ;
	BufferedReader in;
	BufferedWriter out;
	TextView tv;
	
	public MainControllerActivity()
	{
		//tv = (TextView)findViewById(R.id.txtStatus);
	}
	public void sendCommand(View view,String command)
	{
		try {
            //send output msg
            String outMsg = "msg:"+"Tag = "+view.getTag() + ", cmd = "+command;
            out.write(outMsg);
            out.flush();
            Log.i("RcCarController", "sent: " + outMsg);
            //accept server response
            String inMsg = in.readLine() + System.getProperty("line.separator");
            //tv.setText(inMsg);
            Log.i("RcCarController", "received: " + inMsg);
            //s.close();
        	} catch (UnknownHostException e) {
    	    	e.printStackTrace();
    	    } catch (IOException e) {
    	    	e.printStackTrace();
    	    }        		
	}
	//see http://developer.android.com/guide/topics/ui/ui-events.html#EventListeners
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int eventaction = event.getAction();

	    switch (eventaction) {
	        case MotionEvent.ACTION_DOWN: 
	            // finger touches the screen
	        	sendCommand(v,"down");
	        	Log.e("RcCarController", "down");
	        	break;

	        case MotionEvent.ACTION_MOVE:
	            // finger moves on the screen
	        	sendCommand(v,"move");
	        	Log.e("RcCarController", "move");
	        	break;

	        case MotionEvent.ACTION_UP:   
	            // finger leaves the screen
	        	sendCommand(v,"up");
	        	Log.e("RcCarController", "up");
	        	break;
	    }            	          	
     return false;
    }    
	
	// see http://androidsnippets.com/handle-touch-events-ontouchevent
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = (TextView)findViewById(R.id.txtStatus);
        setContentView(R.layout.activity_main_controller);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
       	
		try {
			if(s==null)	{
				s = new Socket("192.168.0.107", TCP_SERVER_PORT);
			    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			    out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		        String iam = "iam:androidphone";
		        out.write(iam);
		        out.flush();
			}else {
			    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			    out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			}
	    } catch (UnknownHostException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }    	
        
        ImageButton btnForward = (ImageButton)findViewById(R.id.btnForward);
        ImageButton btnReverse = (ImageButton)findViewById(R.id.btnReverse);
        ImageButton btnLeft = (ImageButton)findViewById(R.id.btnLeft);
        ImageButton btnRight = (ImageButton)findViewById(R.id.btnRight);

        btnForward.setOnTouchListener(this);
        btnReverse.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        /*
        		new OnTouchListener() {
        	public void sendCommand(View view,String command)
        	{
        		try {
                    //send output msg
                    String outMsg = "msg:"+"Tag = "+view.getTag() + ", cmd = "+command;
                    out.write(outMsg);
                    out.flush();
                    Log.i("RcCarController", "sent: " + outMsg);
                    //accept server response
                    String inMsg = in.readLine() + System.getProperty("line.separator");
                    tv.setText(inMsg);
                    Log.i("RcCarController", "received: " + inMsg);
                    //s.close();
                	} catch (UnknownHostException e) {
            	    	e.printStackTrace();
            	    } catch (IOException e) {
            	    	e.printStackTrace();
            	    }        		
        	}
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	int eventaction = event.getAction();

        	    switch (eventaction) {
        	        case MotionEvent.ACTION_DOWN: 
        	            // finger touches the screen
        	        	sendCommand(v,"down");
        	        	Log.e("RcCarController", "down");
        	        	break;

        	        case MotionEvent.ACTION_MOVE:
        	            // finger moves on the screen
        	        	sendCommand(v,"move");
        	        	Log.e("RcCarController", "move");
        	        	break;

        	        case MotionEvent.ACTION_UP:   
        	            // finger leaves the screen
        	        	sendCommand(v,"up");
        	        	Log.e("RcCarController", "up");
        	        	break;
        	    }            	          	
             return false;
            }
        });    */    
        
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
/*        
  		if (mCamera != null) {
            mCamera.release()
            mCamera = null;
        }
        */
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        /*
        if (mCamera == null) {
            initializeCamera(); // Local method to handle camera init
        }
        */
    }
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
    }
    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
    }
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        
        // Activity being restarted from stopped state    
    }    
    
    /*Custom Event handlers*/
	
    private static final int TCP_SERVER_PORT = 80;
    private void runTcpClient() {
        try {
            Socket s = new Socket("192.168.0.100", TCP_SERVER_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            //send output msg
            String iam = "iam:androidphone";
            String outMsg = "msg:hello dude how are you?";
            out.write(iam);
            out.flush();
            out.write(outMsg);
            out.flush();
            Log.i("TcpClient", "sent: " + iam);
            Log.i("TcpClient", "sent: " + outMsg);
            //accept server response
            String inMsg = in.readLine() + System.getProperty("line.separator");
            Log.i("TcpClient", "received: " + inMsg);
            //close connection
            s.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //replace runTcpClient() at onCreate with this method if you want to run tcp client as a service
    private void runTcpClientAsService() {
        Intent lIntent = new Intent(this.getApplicationContext(), TcpClientService.class);
        this.startService(lIntent);
    }
}
