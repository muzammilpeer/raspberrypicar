package com.muzamilpeer.raspberrypicar.app.manualdrive;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;

public class ManualDriveFragment extends SherlockFragment {

    View mainView;
    ActionBar actionBar;
    CommonActions ca;
	ArrayList<Object> listProviderArray = new ArrayList<Object>();
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        mainView = inflater.inflate(R.layout.activity_manualdrive, null);
        
        if (savedInstanceState == null) {
            initViews();
            initObjects();
            initListeners();
           // getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        return mainView;
    }
    private void initViews() {
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