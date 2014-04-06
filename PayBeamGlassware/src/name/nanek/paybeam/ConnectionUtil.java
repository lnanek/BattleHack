package name.nanek.paybeam;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionUtil {
	
	private static final String TAG = "ConnectionUtil";

	public static boolean isOnline(final Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo active = cm.getActiveNetworkInfo();
		if ( null == active ) return false;

		boolean online = active.isConnectedOrConnecting();
		Log.i(TAG, "ConnectionUtil#isOnline: online = " + online);
		return online;
	}

	
}
