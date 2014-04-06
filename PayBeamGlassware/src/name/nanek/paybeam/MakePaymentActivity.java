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
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MakePaymentActivity extends Activity {
	
	private static final String LOG_TAG = "MakePaymentActivity";
	
	private static final String SERVER = "http://10.0.2.2:9091/adaptivepaymentssample/BattleHackReadDatabase";

	private TextView text;
	
	private List<Recipient> recipients = new LinkedList<Recipient>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(LOG_TAG, "onCreate");
		
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 

		setContentView(R.layout.activity_make_payment);

		text = (TextView) findViewById(R.id.text);
		
		
		
		// TODO show downloading prompt
		
		
		// TODO do on other thread
		readReceipients();
				
		// TODO pick person by swiping
				
		// TODO make payment		
		
		
	}
	
	public String readReceipients() {
		
		Log.i(LOG_TAG, "readReceipients");

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
	        
	        
	        
	        Gson gson = new Gson();
	        Type collectionType = new TypeToken<List<Recipient>>(){}.getType();
	        recipients = gson.fromJson(builder.toString(), collectionType);
	        
            Log.d("tag", "List: " + recipients);

            // TODO
          //openOptionsMenu();       
	        
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
	
	
	// TODO offer menu items parsed from download
	
    private static final int UPDATE_DATA = 0;
    private static final int ADD_NEW = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

		Log.i(LOG_TAG, "onCreateOptionsMenu");

        menu.add(0,UPDATE_DATA,0,"Update Information");

        menu.add(0,ADD_NEW,1,"Add New Inspection"); 
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

		Log.i(LOG_TAG, "onOptionsItemSelected");

		
        switch(item.getItemId())
        {
           case(UPDATE_DATA):
               Log.d("tag", "update");
               break;
           case(ADD_NEW):
              Log.d("tag", "Add");
               break; 
          } 
        return false; 
        
        
        
    }

}
