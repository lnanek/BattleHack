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

public class MakePaymentToActivity extends Activity {
	
	// TODO more icons and sound effects
	
	private ScreenWaker screenWaker;
	
	private static final String LOG_TAG = "MakePaymentToActivity";
	
    private AudioManager mAudioManager;
	
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

		// Process voice prompt
		final Intent intent = getIntent();
		final ArrayList<String> voiceResults = 
				intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		if ( null != voiceResults ) {

			if ( voiceResults.size() == 0 ) {
				displayTextCard("Please say a name and amount.");
				return;
			}
			
			Log.i(LOG_TAG, "voice result: " + voiceResults.get(0));			
			final String[] words = voiceResults.get(0).split(" ");
			
			if ( words.length == 1 ) {
				displayTextCard("Please say an amount after the name.");
				return;
			}
			
			final String amount = words[words.length - 1];
			String name = "";
			for(int i = 0; i < words.length - 1; i++) {
				name += words[i] + " ";
			}
			displayTextCard("Searching contacts for " + name.trim() 
					+ " to send $" + amount + "...");
			// TODO
			return;
		}
		
		displayTextCard("Please say a name and amount.");
		return;
		
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
        finish();
    }


}
