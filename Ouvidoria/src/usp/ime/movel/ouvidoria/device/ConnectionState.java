package usp.ime.movel.ouvidoria.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionState extends BroadcastReceiver {

	private Context applicationContext;
	private boolean isConnected;
	private String type;

	public ConnectionState(Context applicationContext) {
		this.applicationContext = applicationContext;
		this.isConnected = false;
		this.type = null;
	}
	
	public boolean isConnected () {
		return isConnected;
	}
	
	public String getType () {
		return type;
	}
	
	/* Referências:
	 * (non-Javadoc)
	 * http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-timeouts
	 * http://paragchauhan2010.blogspot.com.br/2012/04/check-network-connection-using.html
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connecivityManager = (ConnectivityManager) applicationContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo currentNetworkInfo = connecivityManager
				.getActiveNetworkInfo();
		if (currentNetworkInfo != null && currentNetworkInfo.isConnectedOrConnecting()) {
			isConnected = true;
			type = currentNetworkInfo.getTypeName();
			Toast.makeText(applicationContext, "Connected/"+type, Toast.LENGTH_LONG)
					.show();
		} else {
			isConnected = false;
			type = null;
			Toast.makeText(applicationContext, "Not Connected",
					Toast.LENGTH_LONG).show();
		}
	}
}
