package name.nanek.paybeam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.glass.app.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MakePaymentActivity extends Activity {
	
	// TODO more icons and sound effects
	
	private ScreenWaker screenWaker;
	
	private static final int MSG_ID_GOT_RECIPIENTS = 1;
	
	private static final int MSG_ID_ERROR = 2;

	private static final String LOG_TAG = "MakePaymentActivity";

	private static final String LOCAL_SERVER = "http://10.0.2.2:9091/adaptivepaymentssample/";

	private static final String HEROKU_SERVER = "http://paybeam.herokuapp.com/";
	
	private static final String SERVER = HEROKU_SERVER;

	private static final String DOWNLOAD_URL = SERVER + "BattleHackReadDatabase";
	
	private static final String SUBMIT_URL = SERVER + "BattleHackCompletePayment";
	
    private AudioManager mAudioManager;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i(LOG_TAG, "handleMessage");

			//if ( null != progress && progress.isShowing() ) {
			//	progress.hide();
			//}
			
			if ( MSG_ID_GOT_RECIPIENTS == msg.what ) {

				Log.i(LOG_TAG, "updating recipients");
				
				recipients = (List<Recipient>) msg.obj;
				if ( null == recipients ) {
					displayTextCard("No recipients found.");
					return;
				}
				
				//displayTextCard(recipients.toString());

				displayTextCard("Found " + recipients.size() + " recipients.");

				invalidateOptionsMenu();
				openOptionsMenu();
				
				return;

			} else if ( MSG_ID_ERROR == msg.what ) {

				Log.i(LOG_TAG, "updating text");

				final String message = (String) msg.obj;
				if ( null != message ) {
					displayTextCard(message);
				}
			}
		}
	};

	private List<Recipient> recipients = new LinkedList<Recipient>();

	//private ProgressDialog progress;
	
	private void displayTextCard(final String message) {
    	final Card card = new Card(this);
    	card.setText(message);
    	setContentView(card.toView());   		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);
        screenWaker = new ScreenWaker(this);        

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		if (!ConnectionUtil.isOnline(this)) {
			displayTextCard("Please connect to internet.");
			return;
		}
		
		// Otherwise download registered recipients and offer swipe list.
		displayTextCard("Searching for recipients...");
		//progress = new ProgressDialog(this);
		//progress.show();
		final Thread thread = new Thread() {
			@Override
			public void run() {
				readReceipients();
			}
		};
		thread.start();
	}
	

    @Override
    public void onResume() {
        super.onResume();
        screenWaker.onResume();
    }
   
    @Override
    public void onPause() {
        super.onPause();
        screenWaker.onPause();
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
				final Message message = handler.obtainMessage(MSG_ID_ERROR, 
						"Error searching (" + statusCode + "). Please try again later.");
				handler.sendMessage(message);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			final Message message = handler.obtainMessage(MSG_ID_ERROR, 
					"Error searching (" + e.getClass().getSimpleName() + "). Please try again later.");
			handler.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			final Message message = handler.obtainMessage(MSG_ID_ERROR, 
					"Error searching (" + e.getClass().getSimpleName() + "). Please try again later.");
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

				final Message message = handler.obtainMessage(MSG_ID_ERROR, 
						"Payment complete!");
				handler.sendMessage(message);

				// TODO complete sound, OK icon

			} else {
				final Message message = handler.obtainMessage(MSG_ID_ERROR, 
						"Error paying (" + statusCode + "). Please try again later.");
				handler.sendMessage(message);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			final Message message = handler.obtainMessage(MSG_ID_ERROR, 
					"Error paying (" + e.getClass().getSimpleName() + "). Please try again later.");
			handler.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			final Message message = handler.obtainMessage(MSG_ID_ERROR, 
					"Error paying (" + e.getClass().getSimpleName() + "). Please try again later.");
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
			
			final String menuTitle = "$" + recipient.amount + " " + recipient.email;
			final MenuItem item = menu.add(0, menuItemId, 0, menuTitle);	
			item.setIcon(R.drawable.ic_menu_send);

			Log.i(LOG_TAG, "adding menu item: " + menuTitle);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Log.i(LOG_TAG, "onOptionsItemSelected");
		mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
		
		final Recipient recipient = recipients.get(item.getItemId());
		displayTextCard("Paying " + recipient.email + " $" + recipient.amount + "...");
		
		final String username = DeviceEmail.get(this);
		//final String username = "lnanek@gmail.com";
		
		//progress.show();
		closeOptionsMenu();
		
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
