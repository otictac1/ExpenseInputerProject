package com.mongoose.studios.expenses;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button balanceButton, submitButton;
	private Spinner financialInstitution, categorySpinner;
	private EditText amountToEnter, vendorToEnter;
	String szImei;
	String phoneNumber;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		szImei = TelephonyMgr.getDeviceId();
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(
						android.R.color.holo_orange_light)));

		if (szImei.equals("99000259209404")) {
			phoneNumber = "4155285867";
			setTitle("Hello Bradley.  Expense Please");
		} else {
			phoneNumber = "4152374146";
			setTitle("Hello LJ! I love you! Expense Please: ");
		}

		Log.i("Device ID", "MyDeviceID is " + szImei);

		financialInstitution = (Spinner) findViewById(R.id.financialInstitutionSpinner);
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		amountToEnter = (EditText) findViewById(R.id.amountEditText);
		vendorToEnter = (EditText) findViewById(R.id.vendorEditText);

		balanceButton = (Button) findViewById(R.id.balanceCheckButton);
		balanceButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String message = "Balance";
				sendSMS(message);

			}
		});

		submitButton = (Button) findViewById(R.id.submitExpenseButton);
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String message = String.valueOf(financialInstitution
						.getSelectedItem())
						+ ","
						+ amountToEnter.getText().toString()
						+ ","
						+ vendorToEnter.getText().toString()
						+ ","
						+ String.valueOf(categorySpinner.getSelectedItem());
				sendSMS(message);
				amountToEnter.setText(null);
				vendorToEnter.setText(null);
			}
		});

	}

	protected void sendSMS(String message) {
		String phoneNumber;

		if (szImei.equals("990002592094047")) {
			phoneNumber = "4155285867";
			setTitle("Hello Bradley");
		} else {
			phoneNumber = "4152374146";
			setTitle("Hello Laura!  I love you!");
		}
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, message, null, null);
			Toast.makeText(getApplicationContext(), "SMS sent.",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}
}
