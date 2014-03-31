package com.muzamilpeer.raspberrypicar.app.login;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.DashboardActivity;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;

public class LoginActivity extends SherlockActivity {

    View mainView;
    Button btnLogin;
    
    ActionBar actionBar;
    CommonActions ca;
	ArrayList<Object> listProviderArray = new ArrayList<Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
        }
        initViews();
        initObjects();
        initListeners();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
	}
	/* 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        mainView = inflater.inflate(R.layout.activity_scancar, null);
        if (savedInstanceState == null) {
            initViews();
            initObjects();
            initListeners();
        }
        return mainView;
    }
    */
    private void initViews() {
    	btnLogin = (Button)findViewById(R.id.btnLogin);
//    	gvDenomination = (GridView) mainView.findViewById(R.id.gvDenomination);
    	
    }
    private void initListeners() {
    	btnLogin.setOnClickListener(mGlobal_OnClickListener);
    }

    private void initObjects() {
    	//Common View 
    	ca = new CommonActions(this);
    }
    
    private boolean validation()
    {
    	boolean isValid = true;
    	return isValid;
    }
    private void callAct() {
        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(i);
    }
 
    //Global On click listener for all views
    final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btnLogin:
                {
                	callAct();
                }
                break;
            }
        }
    };



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isNew", false);
    }
    
}