package com.cix.cloudclient;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayNotificationActivity extends Activity {

	private ArrayList<String> notiList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_notification);
		
		notiList = new ArrayList<String>();
		notiList.add("Notification From Vriti");
		
		Bundle notiBundle = getIntent().getExtras();
		if( notiBundle != null) {
			//String notiTitle = notiBundle.getString("noti_title");
			String notiDetail = notiBundle.getString("noti_detail");
			
			notiList.add(notiDetail);
		}
		
		NotificationAdapter notiAdapter = new NotificationAdapter(notiList);
		
		ListView notiList = (ListView) findViewById(R.id.lvDispNotification);
		notiList.setAdapter(notiAdapter);
		
	}
	

	private class NotificationAdapter extends BaseAdapter {

		private ArrayList<String> notiList = null;

		public NotificationAdapter(final ArrayList<String> list) {
			notiList = list;
		}



		@Override
		public int getCount() {
			return notiList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LinearLayout notiListContainer = new LinearLayout(DisplayNotificationActivity.this);
			TextView tvNotiTitle = new TextView(getApplicationContext());
			tvNotiTitle.setText(notiList.get(position));
			notiListContainer.addView(tvNotiTitle);
			
			return notiListContainer;
			
		}// End of getView()


	};

}
