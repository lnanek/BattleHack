package name.nanek.paybeam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MakePaymentActivity extends Activity {
	
	private static final int MSG_ID_GOT_RECIPIENTS = 1;
	
	private static final int MSG_ID_ERROR = 2;

	private static final String LOG_TAG = "MakePaymentActivity";

	private static final String LOCAL_SERVER = "http://10.0.2.2:9091/adaptivepaymentssample/";

	private static final String HEROKU_SERVER = "http://paybeam.herokuapp.com/";
	
	private static final String SERVER = HEROKU_SERVER;

	private static final String DOWNLOAD_URL = SERVER + "BattleHackReadDatabase";
	
	private static final String SUBMIT_URL = SERVER + "BattleHackCompletePayment";
	
	private TextView text;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i(LOG_TAG, "handleMessage");

			if ( null != progress && progress.isShowing() ) {
				progress.hide();
			}
			
			if ( MSG_ID_GOT_RECIPIENTS == msg.what ) {

				Log.i(LOG_TAG, "updating recipients");
				
				recipients = (List<Recipient>) msg.obj;
				
				text.setText(recipients.toString());
				
				invalidateOptionsMenu();
				
				openOptionsMenu();
				
				return;

			} else {

				Log.i(LOG_TAG, "updating text");

				final String message = (String) msg.obj;
				text.setText(message);
			}
		}
	};

	private List<Recipient> recipients = new LinkedList<Recipient>();

	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.i(LOG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		
		// TODO read voice prompt input, search address book for user, do payment to them

		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy);

		// TODO use card views for more Glass like experience
		setContentView(R.layout.activity_make_payment);

		text = (TextView) findViewById(R.id.text);
		text.setText("Downloading...");

		progress = new ProgressDialog(this);
		progress.show();

		final Thread thread = new Thread() {
			@Override
			public void run() {
				readReceipients();
			}
		};
		thread.start();
	}

	public void readReceipients() {

		Log.i(LOG_TAG, "readReceipients");

		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(DOWNLOAD_URL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));

				StringBuilder builder = new StringBuilder();				
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				Log.d(LOG_TAG, "Downloaded JSON: " + builder.toString());

				
				Gson gson = new Gson();				
				Type collectionType = new TypeToken<LinkedList<Recipient>>() {}.getType();								
				final List<Recipient> downloadedRecipients = gson.fromJson(builder.toString(), collectionType);
				Log.d(LOG_TAG, "List: " + downloadedRecipients);

				final Message message = handler.obtainMessage(
						MSG_ID_GOT_RECIPIENTS, downloadedRecipients);
				handler.sendMessage(message);


			} else {
				final Message message = handler.obtainMessage(MSG_ID_ERROR, "Failed to download file");
				handler.sendMessage(message);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			final Message message = handler.obtainMessage(MSG_ID_ERROR, "Failed to download file: " + e.getClass().getName());
			handler.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			final Message message = handler.obtainMessage(MSG_ID_ERROR, "Failed to download file: " + e.getClass().getName());
			handler.sendMessage(message);
		}
	}
	
	public void completePayment(final String recipient, final String username, final String amount) {

		Log.i(LOG_TAG, "completePayment");

		// TODO send DB index for recipient,
		// google auth token for user
		// for better security
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(SUBMIT_URL
				+ "?recipient=" + recipient
				+ "&username=" + username
				+ "&amount=" + amount
				
				);
		try {
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {

				final Message message = handler.obtainMessage(MSG_ID_ERROR, "Payment complete");
				handler.sendMessage(message);

				// TODO complete sound, OK icon

			} else {
				final Message message = handler.obtainMessage(MSG_ID_ERROR, "Failed to complete");
				handler.sendMessage(message);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			final Message message = handler.obtainMessage(MSG_ID_ERROR, "Failed to complete: " + e.getClass().getName());
			handler.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			final Message message = handler.obtainMessage(MSG_ID_ERROR, "Failed to complete: " + e.getClass().getName());
			handler.sendMessage(message);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(LOG_TAG, "onCreateOptionsMenu");
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Log.i(LOG_TAG, "onPrepareOptionsMenu");
		
		menu.clear();

		Log.i(LOG_TAG, "recipients.size = " + recipients.size());

		for(int index = 0; index < recipients.size(); index++) {
			final Recipient recipient = recipients.get(index);
			final int menuItemId = index;
			
			final String menuTitle = recipient.email + " - $" + recipient.amount;
			menu.add(0, menuItemId, 0, menuTitle);	
			

			Log.i(LOG_TAG, "adding menu item: " + menuTitle);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Log.i(LOG_TAG, "onOptionsItemSelected");
		
		final Recipient recipient = recipients.get(item.getItemId());
		text.setText("Selected = " + recipient.email);
		
		//final String username = DeviceEmail.get(this);
		final String username = "lnanek@gmail.com";
		
		progress.show();
		final Thread thread = new Thread() {
			@Override
			public void run() {
				completePayment(recipient.email, username, recipient.amount);
			}
		};
		thread.start();
		
		return false;
	}

}
