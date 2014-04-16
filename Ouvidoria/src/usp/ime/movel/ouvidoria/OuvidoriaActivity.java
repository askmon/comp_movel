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
		registerReceivers();
	}

	protected BatteryState getBatteryState() {
		return batteryState;
	}

	protected ConnectionState getConnectionState() {
		return connectionState;
	}

	private void registerReceivers () {
		registerReceiver(batteryState, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		registerReceiver(connectionState, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	public void onRestart() {
		super.onRestart();
		registerReceivers();
	}

	@Override
	public void onStop() {
		// Hammer time.
		super.onStop();
		unregisterReceiver(batteryState);
		unregisterReceiver(connectionState);
	}

}
