
package com.muzamilpeer.raspberrypicar.app.menu;


import com.muzamilpeer.raspberrypicar.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment {

    View menuView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        menuView = inflater.inflate(R.layout.fragment_menu, null);

        return menuView;
    }
}
