package name.nanek.paybeam;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

public class DeviceEmail {

	public static String get(final Context context) {
		
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		        if (null != account.name && !"".equals(account.name.trim()) ) {
		        	return account.name;
		        }
		    }
		}		
		return null;
		
	}
	
}
