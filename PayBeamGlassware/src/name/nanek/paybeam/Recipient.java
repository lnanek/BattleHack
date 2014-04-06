package name.nanek.paybeam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Recipient {
	
	public String amount;
	
	public String email;
	
	public String status;
	
	@Override
	public String toString() {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
        final String json = gson.toJson(this);
    
        return json;
	}

}
