package com.muzamilpeer.raspberrypicar.app.accelerometerdrive;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.view.SpeedPreference;
import com.muzamilpeer.raspberrypicar.view.SpeedometerView;

public class AccelerometerDriveFragment extends SherlockFragment {

    View mainView;
    ActionBar actionBar;
    CommonActions ca;
	ArrayList<Object> listProviderArray = new ArrayList<Object>();
	 

	private long delay = 10000;

	SpeedometerView mView;

	ImageView arrowImag;

	static Handler handler;

	String result;

	int startAngle;

	String priceStr, speedValueStr;

	Thread threadMainMeter;

	Button changeNeedleValueBtn;

	SpeedPreference speedPreference;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        mainView = inflater.inflate(R.layout.activity_accelerometerdrive, null);
        if (savedInstanceState == null) {
            initViews();
            initObjects();
            initListeners();
            
        	initUI();
    		speedPreference = new SpeedPreference(getSherlockActivity());
    		speedPreference.setPreviousNeedleValue("0");
    		moveNeedle();            
        }
        return mainView;
    }	

	private void moveNeedle() {

		handler = new Handler() {

			public void handleMessage(android.os.Message msg) {

				Bundle b = msg.getData();
				int key = b.getInt("angle_in_degrees", 0);

				if (key == 0) {
					
				} else {
					mView.calculateAngleOfDeviation(key);
				}

			};
		};
		handler.postDelayed(null, delay);

		threadMainMeter = new Thread(new Runnable() {

			@Override
			public void run() {

				startAngle = Integer.parseInt(speedPreference
						.getPreviousNeedleValue());

				generateValue();

				if (Integer.parseInt(speedValueStr) > 100) {
					speedValueStr = "100";
				}
				if (startAngle > Integer.parseInt(speedValueStr)) {
					for (int i = startAngle; i >= Integer
							.parseInt(speedValueStr); i = i - 1) {

						try {
							Thread.sleep(15);
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putInt("angle_in_degrees", i);
							msg.setData(b);
							handler.sendMessage(msg);

						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}

				} else {
					for (int i = startAngle; i <= Integer
							.parseInt(speedValueStr); i = i + 1) {

						try {
							Thread.sleep(15);
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putInt("angle_in_degrees", i);
							msg.setData(b);
							handler.sendMessage(msg);

						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}
				}
			}
		});

		threadMainMeter.start();

	}

	private void generateValue() {

		Random r = new Random();
		speedValueStr = String.valueOf(r.nextInt(100 - 10) + 10);
		speedPreference.setPreviousNeedleValue(speedValueStr);

	}

	private void initUI() {

		mView = (SpeedometerView)mainView.findViewById(R.id.speedometer_view);

		changeNeedleValueBtn = (Button) mainView.findViewById(R.id.change_needle_value_btn);
		changeNeedleValueBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				moveNeedle();

			}
		});
	}
	
    private void initViews() {
//    	gvDenomination = (GridView) mainView.findViewById(R.id.gvDenomination);
    	
    }
    private void initListeners() {
    }

    private void initObjects() {
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
            /*
                case R.id.btnBuy:
                    //Buy Button Pressed call Buy Services and show confirmation dialog
                	//showBuyVoucherConfirmationDialog();
                break;
                */
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
}