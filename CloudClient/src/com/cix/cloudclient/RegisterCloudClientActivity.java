package com.cix.cloudclient;


import com.cix.cloudclient.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterCloudClientActivity extends Activity {

	Network net;
	Button btnRegister;	
	Button btnUnRegister;	
	EditText editTextVritiId;
	String vritiId;
	AlertDialogManager alert = new AlertDialogManager();	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_client);
		
		net = new Network(getApplicationContext());
		if (!net.isNetworkConnected()) {
			alert.showAlertDialog(RegisterCloudClientActivity.this,
					"Network Connection Error",
					"Please Check the connection", false);
			return;
		}

		if (Utils.SERVER_URL == null || Utils.SENDER_ID == null || Utils.SERVER_URL.length() == 0
				|| Utils.SENDER_ID.length() == 0) {

			alert.showAlertDialog(RegisterCloudClientActivity.this, "Incorrect Configuration",
					"Please set your Server URL and GCM Sender ID", false);
			 return;
		}

		
		editTextVritiId = (EditText)findViewById(R.id.idEditText);
		
		
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
					vritiId = editTextVritiId.getText().toString();
					Intent i = new Intent(getApplicationContext(), CloudClientMainActivity.class);
					i.putExtra("action", "reg");
					i.putExtra("vid", vritiId);
					startActivity(i);
					finish();

			}
		});
		
		
		
		btnUnRegister = (Button) findViewById(R.id.btnUnRegister);
		btnUnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
					Intent i = new Intent(getApplicationContext(), CloudClientMainActivity.class);
					i.putExtra("action", "unreg");
					i.putExtra("vid", vritiId);					
					startActivity(i);
					finish();

			}
		});		
		
	}

	
	
	
	

}
