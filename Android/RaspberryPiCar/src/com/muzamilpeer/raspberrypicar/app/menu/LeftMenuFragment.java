package com.muzamilpeer.raspberrypicar.app.menu;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.DashboardActivity;
import com.muzamilpeer.raspberrypicar.app.accelerometerdrive.AccelerometerDriveFragment;
import com.muzamilpeer.raspberrypicar.app.camdrive.CamDriveFragment;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.manualdrive.ManualDriveFragment;
import com.muzamilpeer.raspberrypicar.app.scan.ScanCarFragment;
import com.muzamilpeer.raspberrypicar.app.smartcamdrive.SmartCamDriveFragment;
import com.muzamilpeer.raspberrypicar.app.smartdrive.SmartDriveFragment;

public class LeftMenuFragment extends SherlockFragment {
	
    View menuView;
    ArrayList<String> menuDataSource;
    ArrayAdapter<String> menuListViewAdaptor;
    
    CommonActions ca;
    
    ListView menuListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        menuView = inflater.inflate(R.layout.left_fragment_menu, null);

        if (savedInstanceState == null) {
        }
        initViews();
        initObjects();
        initListeners();

        return menuView;    	
    }
    
    private void initViews() {
    	menuListView = (ListView)menuView.findViewById(R.id.leftMenuListView);
    }
    private void initObjects() {
		menuDataSource = new ArrayList<String>();
		menuDataSource.add("Home");
		menuDataSource.add("* Scan Car");
		menuDataSource.add("* Touch Drive");
		menuDataSource.add("* Accelerometer Drive");
		menuDataSource.add("* Smart Drive");
		menuDataSource.add("* Cam Drive");
		menuDataSource.add("* SmartCam Drive");
		ca = new CommonActions(getActivity());
		//ListView Adaptor
		menuListViewAdaptor = new ArrayAdapter<String>(menuView.getContext(), android.R.layout.simple_list_item_1, menuDataSource);
		menuListView.setAdapter(menuListViewAdaptor);
      }
    
      
      private void initListeners() {
    	  menuListView.setOnItemClickListener(new OnItemClickListener() {
  			public void onItemClick(AdapterView<?> parent, View view,
  					int position, long id) {
  			    //Load Respective Screen on Main Fragment View
  				switch (position) {
				case 1:
					DashboardActivity.refreshMainViewByNew(new ScanCarFragment());
					break;
				case 2:
					DashboardActivity.refreshMainViewByNew(new ManualDriveFragment());
					break;
				case 3:
					DashboardActivity.refreshMainViewByNew(new AccelerometerDriveFragment());
					break;
				case 4:
					DashboardActivity.refreshMainViewByNew(new SmartDriveFragment());
					break;
				case 5:
					DashboardActivity.refreshMainViewByNew(new CamDriveFragment());
					break;
				case 6:
					DashboardActivity.refreshMainViewByNew(new SmartCamDriveFragment());
					break;

				default:
					DashboardActivity.refreshMainViewByNew(new ScanCarFragment());
					break;
				}
  					
  			}
  		});    	  
      }
}
