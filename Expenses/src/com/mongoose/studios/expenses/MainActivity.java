package com.mongoose.studios.expenses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener,
		DialogInterface.OnClickListener {

	private Button balanceButton, submitButton;
	private Spinner financialInstitution, categorySpinner;
	private EditText amountToEnter, vendorToEnter;
	private ImageButton favoritesButton;
	String szImei;
	String phoneNumber;
	AlertDialog ad;

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
						android.R.color.holo_blue_dark)));

		if (szImei.equals("99000259209404")) {
			phoneNumber = "4155285867";
			setTitle("Hello Bradley.  Expense Please");
		} else {
			phoneNumber = "4152374146";
			setTitle("Hello LJ! I love you! Expense Please: ");
		}

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
						+ amountToEnter.getText().toString().trim()
						+ ","
						+ vendorToEnter.getText().toString().trim()
						+ ","
						+ String.valueOf(categorySpinner.getSelectedItem());
				sendSMS(message);
				amountToEnter.setText(null);
				vendorToEnter.setText(null);
				financialInstitution.setSelection(0);
				categorySpinner.setSelection(0);
			}
		});

		favoritesButton = (ImageButton) findViewById(R.id.favoritesIB);
		favoritesButton.setOnClickListener(this);

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

	@Override
	public void onClick(View v) {
		final String[] favorites = { "Kroger", "Weigel's", "Target", "Amazon" , "Chick-Fil-A" , "Food City" , "Lowes" 
				, "Subway" , "Shell" , "Wendy's" , "Stefano's" , "Trader Joe's" , "Regal Cinemas", "Ingles" };

		AlertDialog.Builder b = new Builder(this);
		b.setTitle("Favorite Places to Shop");
		b.setNegativeButton("Cancel", this);
		b.setCancelable(true);
		b.setItems(favorites, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

				vendorToEnter.setText(favorites[which]);
				switch(which){
				case 0: case 5: case 11: case 13:
					categorySpinner.setSelection(8, true);
					break;
				case 1: case 8:
					categorySpinner.setSelection(3,true);
					break;
				case 4: case 7: case 9: case 10:
					categorySpinner.setSelection(7, true);
					break;
				case 12:
					categorySpinner.setSelection(6,true);
					break;
				
				
				}
				

			}

		});

		b.show();

		/*
		 * AlertDialog ad = new AlertDialog.Builder(this) .setItems(favorites,
		 * this) .setTitle("Favorites Menu")
		 * .setMessage("Choose one of your favorites") .setCancelable(true)
		 * .setPositiveButton("Yes", this) .setNegativeButton("No", this)
		 * .create(); ad.show();
		 */
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();

	}
}
