package com.muzamilpeer.raspberrypicar.app.scan;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.muzamilpeer.raspberrypicar.R;
import com.muzamilpeer.raspberrypicar.app.common.SystemConstants;
import com.muzamilpeer.raspberrypicar.model.ServerInfoModel;

public class CarsListViewAdapter extends ArrayAdapter<ServerInfoModel> {
	Context context;
	int layoutResourceId;
	ArrayList<ServerInfoModel> data = new ArrayList<ServerInfoModel>();

	public CarsListViewAdapter(Context context, int resource,
			ArrayList<ServerInfoModel> objects) {
		super(context, resource, objects);
		this.layoutResourceId = resource;
		this.context = context;
		this.data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		HistoryRowHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new HistoryRowHolder();
			holder.txtRowIoT = (TextView) row.findViewById(R.id.txtRowIot);
			holder.txtRowServerIp = (TextView) row.findViewById(R.id.txtRowServerIp);
			holder.txtRowServerPort = (TextView) row.findViewById(R.id.txtRowServerPort);
			row.setTag(holder);
		} else {
			holder = (HistoryRowHolder) row.getTag();
		}
		ServerInfoModel item = (ServerInfoModel)data.get(position);
		holder.txtRowIoT.setText(SystemConstants.isIoT ? "Internet" : "Intranet");
		holder.txtRowServerIp.setText(item.getServerIP());
		holder.txtRowServerPort.setText(item.getServerPort());
 		
		//Bitmap bitmapIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		//holder.imageItem.setImageBitmap(item.getImage());
		return row;

	}
	static class HistoryRowHolder {
		TextView txtRowIoT;
		TextView txtRowServerIp;
		TextView txtRowServerPort;
	}	
}
