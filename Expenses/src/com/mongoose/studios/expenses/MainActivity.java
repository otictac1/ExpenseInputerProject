package com.mongoose.studios.expenses;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener,
		DialogInterface.OnClickListener {

	private Spinner financialInstitution, categorySpinner;
	private EditText amountToEnter, vendorToEnter;
	private ImageButton favoritesButton;
	private String szImei;
	private String phoneNumber;
	AlertDialog ad;
	String balance;
	TextView abtv;

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
		getActionBar().setDisplayShowTitleEnabled(false);

		try {
			balance = new getBalance().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		if (szImei.equals("99000259209404")) {
			phoneNumber = "4155285867";
		} else {
			phoneNumber = "4152374146";
		}

		financialInstitution = (Spinner) findViewById(R.id.financialInstitutionSpinner);
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		amountToEnter = (EditText) findViewById(R.id.amountEditText);
		vendorToEnter = (EditText) findViewById(R.id.vendorEditText);

		favoritesButton = (ImageButton) findViewById(R.id.favoritesIB);
		favoritesButton.setOnClickListener(this);

	}

	protected void sendSMS(String message) {
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, message, null, null);
			Toast.makeText(getApplicationContext(), "Request Sent!",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		final String[] favorites = { "Kroger", "Weigel's", "Target", "Amazon",
				"Chick-Fil-A", "Food City", "Lowes", "Subway", "Shell",
				"Wendy's", "Stefano's", "Trader Joe's", "Regal Cinemas",
				"Ingles" };

		AlertDialog.Builder b = new Builder(this);
		b.setTitle("Favorite Places to Shop");

		// Need to add an icon
		b.setIcon(null);

		b.setNegativeButton("Cancel", this);
		b.setCancelable(true);
		b.setItems(favorites, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

				vendorToEnter.setText(favorites[which]);
				switch (which) {
				case 0:
				case 5:
				case 11:
				case 13:
					categorySpinner.setSelection(8, true);
					break;
				case 1:
				case 8:
					categorySpinner.setSelection(3, true);
					break;
				case 4:
				case 7:
				case 9:
				case 10:
					categorySpinner.setSelection(7, true);
					break;
				case 12:
					categorySpinner.setSelection(6, true);
					break;
				case 6:
					categorySpinner.setSelection(13, true);
					break;

				}

			}

		});

		b.show();

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();

	}

	private class getBalance extends AsyncTask<Void, Void, String> {
		String url = "https://dl.dropboxusercontent.com/u/110160094/balance_information/balance.txt";

		@Override
		protected String doInBackground(Void... params) {
			return (getOnline(url));

		}
	}

	public String getOnline(String urlString) {
		URLConnection feedUrl;
		try {
			feedUrl = new URL(urlString).openConnection();
			InputStream is = feedUrl.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "");
			}
			is.close();

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		abtv = new TextView(this);
		abtv.setText(getString(R.string.balance) + " " + balance);
		abtv.setPadding(5, 0, 10, 0);
		abtv.setTypeface(null, Typeface.BOLD);
		abtv.setTextColor(Color.WHITE);
		abtv.setTextSize(13);
		menu.add(0, 1, 1, R.string.balance).setActionView(abtv)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		int id = item.getItemId();
		if (id == R.id.action_settings) {

			if (amountToEnter.getText().toString().trim().equals("")
					|| vendorToEnter.getText().toString().trim().equals("")) {
				Toast.makeText(this, "Please Enter All Required Information",
						Toast.LENGTH_LONG).show();
			}

			else {
				String message = String.valueOf(financialInstitution
						.getSelectedItem())
						+ ","
						+ amountToEnter.getText().toString().trim()
						+ ","
						+ vendorToEnter.getText().toString().trim()
						+ ","
						+ String.valueOf(categorySpinner.getSelectedItem());
				sendSMS(message);
				balance = String.valueOf(Double.parseDouble(balance)
						- Double.parseDouble(amountToEnter.getText().toString()
								.trim()));
				abtv.setText(getString(R.string.balance) + " " + balance);
				amountToEnter.setText(null);
				vendorToEnter.setText(null);
				financialInstitution.setSelection(0);
				categorySpinner.setSelection(0);
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
