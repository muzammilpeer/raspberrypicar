package com.muzamilpeer.raspberrypicar.app.menu;

import java.util.ArrayList;

import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.DashboardActivity;
import com.muzamilpeer.raspberrypicar.app.common.CommonActions;
import com.muzamilpeer.raspberrypicar.app.help.HelpFragment;
import com.muzamilpeer.raspberrypicar.app.settings.SettingsFragment;
import com.muzamilpeer.raspberrypicar.app.signout.SignOutFragment;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class RightMenuFragment extends Fragment {
	
	
    View menuView;
    ArrayList<String> menuDataSource;
    ArrayAdapter<String> menuListViewAdaptor;
    
    CommonActions ca;
    
    ListView menuListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        menuView = inflater.inflate(R.layout.right_fragment_menu, null);

        if (savedInstanceState == null) {
            initViews();
            initObjects();
            initListeners();
        }
        return menuView;    	
    }
    private void initViews() {
    	menuListView = (ListView)menuView.findViewById(R.id.rightMenuListView);
    }
    private void initObjects() {
		menuDataSource = new ArrayList<String>();
		menuDataSource.add("Settings Panel");
		menuDataSource.add("Settings");
		menuDataSource.add("Help");
		menuDataSource.add("SignOut");
		
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
					DashboardActivity.refreshMainViewByNew(new SettingsFragment());
					break;
				case 2:
					DashboardActivity.refreshMainViewByNew(new HelpFragment());
					break;
				case 3:
					DashboardActivity.refreshMainViewByNew(new SignOutFragment());
					break;
				default:
					DashboardActivity.refreshMainViewByNew(new SettingsFragment());
					break;
				}
  					
  			}
  		});    	  
      }
}