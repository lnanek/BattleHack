package name.nanek.paybeam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

public class MakePaymentActivity extends Activity {
	
	private static final String SERVER = "http://10.0.2.2:9091/adaptivepaymentssample/BattleHackReadDatabase";

	private TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 

		setContentView(R.layout.activity_make_payment);

		text = (TextView) findViewById(R.id.text);
		
		readReceipients();
				
		// TODO pick person by swiping
				
		// TODO make payment		
	}
	
	public String readReceipients() {
	    StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(SERVER);
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();

	      
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	        
	        text.setText(builder);
	        
	      } else {
	        text.setText("Failed to download file");
	      }
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	      text.setText("Failed to download file: " + e.getClass().getName());
	    } catch (IOException e) {
	      e.printStackTrace();
	      text.setText("Failed to download file: " + e.getClass().getName());
	    }
	    
	    return builder.toString();
	  }

}
