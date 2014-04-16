package usp.ime.movel.ouvidoria;

import usp.ime.movel.ouvidoria.device.BatteryState;
import usp.ime.movel.ouvidoria.device.ConnectionState;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class OuvidoriaActivity extends Activity {

	private BatteryState batteryState;
	private ConnectionState connectionState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		batteryState = new BatteryState();
		connectionState = new ConnectionState(this);
		registerReceiver(batteryState, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		registerReceiver(connectionState, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	BatteryState getBatteryState() {
		return batteryState;
	}

	ConnectionState getConnectionState() {
		return connectionState;
	}

	@Override
	public void onStop() {
		// Hammer time.
		unregisterReceiver(batteryState);
		unregisterReceiver(connectionState);
		super.onStop();
	}

}
